package com.quad.project.uber.uberApp.strategies;

import com.quad.project.uber.uberApp.dto.RideRequestDto;
import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

     List<Driver> findMatchingDrivers(RideRequest rideRequest);
}
