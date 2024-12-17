package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.DriverRideDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.entities.RideRequest;
import com.quad.project.uber.uberApp.entities.User;
import com.quad.project.uber.uberApp.enums.RideRequestStatus;
import com.quad.project.uber.uberApp.enums.RideStatus;
import com.quad.project.uber.uberApp.enums.Role;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.repository.DriverRepository;
import com.quad.project.uber.uberApp.service.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingService ratingService;

    @Override
    public DriverRideDto acceptRide(Long rideRequestId) {
        // One of the Ten Rider accepts the ride
        // 1.find the rideRequest from the db, which was created and stored when rider requested for a ride.
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);
        // 2.check the status of the rideRequest is pending or no , if not then throw error.
        if(!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
            throw new RuntimeException("RideRequest cannot be accepted, status is "+ rideRequest.getRideRequestStatus());
        }
        // 3.check if the accepted driver is available or not, if not then throw exception.
        Driver currentDriver = getCurrentDriver();
        if (!currentDriver.getAvailable()){
            throw new RuntimeException("Current Driver is Unavailable ");
        }

        // 4. mark the AVAILABILITY of the DRIVER as Busy/unavailable.
        // ??? :: why not update the driver : answer: save, updates the tuple.
        Driver savedDriver = updatedDriverAvailability(currentDriver, false);

        // 5. Now Create the actual Ride Object
        Ride ride = rideService.createNewRide(rideRequest, savedDriver);

        // 6. return the RideDto to the user.
        return modelMapper.map(ride, DriverRideDto.class);
    }

    @Override
    public DriverRideDto startRide(Long rideId, String otp) {
        //1. get the ride
        Ride ride = rideService.getRideById(rideId);
        // 2. check if the driver owns this ride or not
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot start the ride as he has not accepted it earlier");
        }
        // 3. check the ride status, and start the ride only if status is confirmed.
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Ride status is not CONFIRMED hence cannot be started, status : " + ride.getRideStatus());
        }
        // 4. validate the otp, otp -> got through user; ride.getOtp() -> got from the ride stored in db.
        if(!otp.equals(ride.getOtp())){
            throw new RuntimeException("Invalid Otp , otp : " +otp);
        }

        // 5. update the ride status to ONGOING and also add the started time
        ride.setStartedAt(LocalDateTime.now());
        Ride savedRide = rideService.updateRideStatus(ride,RideStatus.ONGOING);

        // 6. create a Payment Object for this ride
        paymentService.createNewPayment(savedRide);

        // 7. create a RatingObject for this ride
        ratingService.createNewRating(ride);

        return modelMapper.map(savedRide, DriverRideDto.class);
    }


    @Transactional
    @Override
    public DriverRideDto endRide(Long rideId) {
        // 1. get the ride
        Ride ride = rideService.getRideById(rideId);

        // 2. get the driver and validate
        Driver driver = getCurrentDriver();
        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot end this ride as he has not accepted it earlier");
        }

        // 3. check the ride status
        if(!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("Ride status is not in ONGOING status, hence cannot be ended, status : " + ride.getRideStatus());
        }

        ride.setEndedAt(LocalDateTime.now());
        Ride updatedRide = rideService.updateRideStatus(ride,RideStatus.ENDED);
        updatedDriverAvailability(driver,true);

        // process the payment for the ride
        paymentService.processPayment(updatedRide);

        rideService.updateRideStatus(updatedRide,RideStatus.ENDED);


        return modelMapper.map(updatedRide, DriverRideDto.class);
    }

    @Override
    public DriverRideDto cancelRide(Long rideId) {
        // get the ride
        Ride ride = rideService.getRideById(rideId);

        // get current Driver
        Driver currentDriver = getCurrentDriver();

        // check is the ride belongs to this Driver
        if(!currentDriver.equals(ride.getDriver())){
            throw new RuntimeException("The ride doesn't belongs to this Driver");
        }

        // check the ride status
        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("Cancel this ride isn't possible, rideStatus : " + ride.getRideStatus());
        }

        // finally cancel the ride and set the driver available field to true.
        rideService.updateRideStatus(ride,RideStatus.CANCELLED);
        updatedDriverAvailability(currentDriver,true);

        return modelMapper.map(ride, DriverRideDto.class);
    }

    @Override
    public RiderDto rateRider(Long rideId, Integer rating) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!driver.equals(ride.getDriver())){
            throw new RuntimeException("Driver cannot Rate this rider as he has not accepted it earlier" +
                    " || Driver does no owns this ride");
        }

        // check the ride status
        if(!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new RuntimeException("Ride status is not in ENDED status, hence cannot be Rated, status : " + ride.getRideStatus());
        }

        return ratingService.rateRider(ride,rating);
    }

    @Override
    public DriverDto getMyProfile() {
        Driver currentDriver = getCurrentDriver();
        return modelMapper.map(currentDriver,DriverDto.class);
    }

    @Override
    public Page<DriverRideDto> getAllMyRides(PageRequest pageRequest) {
        Driver currentDriver = getCurrentDriver();
        // get a Page of ride from the rideService and converting this into DriverRideDto Page
        return rideService.getAllRidesOfDriver(currentDriver,pageRequest)
                .map( ride -> modelMapper.map(ride, DriverRideDto.class));
    }

    public Driver getCurrentDriver() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return driverRepository.findByUser(user)
                .orElseThrow(()-> new ResourceNotFoundException("Driver not associated with user with id :"+ user
                        .getId()));
    }

    @Override
    public Driver updatedDriverAvailability(Driver driver, boolean available) {
        // BAD EXAMPLE : Used when Rider was trying to cancel the ride
        // ps : we just passed the driver from the RiderService itself to save a DB call.
//        Driver driver = getCurrentDriver();
//        driver.setAvailable(available);

        driver.setAvailable(available);
        return driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }


}
