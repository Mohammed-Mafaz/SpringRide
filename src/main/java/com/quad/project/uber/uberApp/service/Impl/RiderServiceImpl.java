package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.RiderRideDto;
import com.quad.project.uber.uberApp.dto.RideRequestDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.*;
import com.quad.project.uber.uberApp.enums.RideRequestStatus;
import com.quad.project.uber.uberApp.enums.RideStatus;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.repository.RideRequestRepository;
import com.quad.project.uber.uberApp.repository.RiderRepository;
import com.quad.project.uber.uberApp.service.DriverService;
import com.quad.project.uber.uberApp.service.RatingService;
import com.quad.project.uber.uberApp.service.RideService;
import com.quad.project.uber.uberApp.service.RiderService;
import com.quad.project.uber.uberApp.strategies.RideStrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;

    private final RideStrategyManager rideStrategyManager;

    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;

    private final RideService rideService;
    private final DriverService driverService;
    private final RatingService ratingService;


    @Override
    @Transactional
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        // getting the current rider with auth
        Rider rider = getCurrentRider();

        RideRequest rideRequest = modelMapper.map(rideRequestDto, RideRequest.class);
        rideRequest.setRideRequestStatus(RideRequestStatus.PENDING);
        rideRequest.setRider(rider);
//        rideRequest.setRequestedTime(LocalDateTime.now());

        // calculate the fare
        Double fare = rideStrategyManager.rideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);

        // save the ride request without driver
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);

        List<Driver> driverList = rideStrategyManager
                .driverMatchingStrategy(rider.getRating()).findMatchingDrivers(rideRequest);

        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }

    @Override
    public RiderRideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);

        Rider currentRider = getCurrentRider();

        // if currentRider is not same as the one who made this request.
        if(!currentRider.equals(ride.getRider())){
            throw new RuntimeException("This ride does not belongs to this Rider , rideId : " + rideId);
        }

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("This ride cannot be Cancelled , rideStatus : "+ride.getRideStatus());
        }

        driverService.updatedDriverAvailability(ride.getDriver(), true);

        // note
         Ride savedRide = rideService.updateRideStatus(ride,RideStatus.CANCELLED);


        return modelMapper.map(savedRide,RiderRideDto.class);
    }

    @Override
    public DriverDto rateDriver(Long rideId, Integer rating) {

        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!rider.equals(ride.getRider())){
            throw new RuntimeException("Rider does no owns this ride");
        }

        // check the ride status
        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is not in ENDED status, hence cannot be Rated, status : " + ride.getRideStatus());
        }

        return ratingService.rateDriver(ride,rating);
    }

    @Override
    public RiderDto getMyProfile() {
        Rider rider = getCurrentRider();
        return modelMapper.map(rider,RiderDto.class);
    }

    @Override
    public Page<RiderRideDto> getAllMyRides(PageRequest pageRequest) {
        Rider rider = getCurrentRider();
        return rideService.getAllRidesOfRider(rider,pageRequest)
                .map(ride -> modelMapper.map(ride,RiderRideDto.class));
    }

    @Override
    public Rider createNewRider(User user) {
        Rider rider = Rider
                .builder()
                .user(user)
                .rating(0.0)
                .build();

        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
//        assert riderRepository != null; // don't know why placed here (suggested by ide)
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return riderRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Rider not associated with user with id :"+ user
                        .getId()));
    }


}
