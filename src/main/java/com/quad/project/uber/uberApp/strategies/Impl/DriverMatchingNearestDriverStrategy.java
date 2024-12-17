package com.quad.project.uber.uberApp.strategies.Impl;

import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.RideRequest;
import com.quad.project.uber.uberApp.repository.DriverRepository;
import com.quad.project.uber.uberApp.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        driverRepository.findTenMatchingDrivers(rideRequest.getPickupLocation());
        return List.of();
    }
}
