package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.DriverRideDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {

    DriverRideDto acceptRide(Long rideRequestId);

    DriverRideDto startRide(Long rideId, String otp);

    DriverRideDto endRide(Long rideId);

    DriverRideDto cancelRide(Long rideId);

    RiderDto rateRider(Long rideId, Integer rating);

    DriverDto getMyProfile();

    Page<DriverRideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver updatedDriverAvailability(Driver driver, boolean available);

    Driver createNewDriver(Driver driver);
}
