package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.dto.RideRequestDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.entities.RideRequest;
import com.quad.project.uber.uberApp.entities.Rider;
import com.quad.project.uber.uberApp.enums.RideRequestStatus;
import com.quad.project.uber.uberApp.enums.RideStatus;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.repository.RideRepository;
import com.quad.project.uber.uberApp.service.RideRequestService;
import com.quad.project.uber.uberApp.service.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;
    private final RideRequestService rideRequestService;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new ResourceNotFoundException("Ride not found with id " + rideId));
    }

    @Override
    public void matchWithDriver(RideRequestDto rideRequestDto) {

    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED);
        rideRequestService.update(rideRequest);

        Ride ride = modelMapper.map(rideRequest,Ride.class);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setDriver(driver);
        ride.setOtp(generateRandomOTP());
        ride.setId(null); // while converting rideRequest to ride, id field will also be filled.

        return rideRepository.save(ride);
    }

    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    public String generateRandomOTP(){
        Random random = new Random();
        int otpInt = random.nextInt(10000); // 0 - 9999
        return String.format("%04d",otpInt); // will empty places with 0 [4 digit]
    }
}

