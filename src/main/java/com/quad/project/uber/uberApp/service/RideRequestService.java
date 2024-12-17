package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.entities.RideRequest;

public interface RideRequestService {

    public RideRequest findRideRequestById(Long rideRequestId);

    public void update(RideRequest rideRequest);
}
