package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.RiderRideDto;
import com.quad.project.uber.uberApp.dto.RideRequestDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.Rider;
import com.quad.project.uber.uberApp.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    RiderRideDto cancelRide(Long rideId);

    DriverDto rateDriver(Long rideId, Integer rating);

    RiderDto getMyProfile();

    Page<RiderRideDto> getAllMyRides(PageRequest pageRequest);

    Rider createNewRider(User user);

    Rider getCurrentRider();
}
