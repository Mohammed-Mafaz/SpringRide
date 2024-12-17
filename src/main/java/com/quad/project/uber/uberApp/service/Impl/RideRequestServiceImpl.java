package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.entities.RideRequest;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.repository.RideRequestRepository;
import com.quad.project.uber.uberApp.service.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("RideRequest not found with id : "+ rideRequestId));
    }

    @Override
    public void update(RideRequest rideRequest) {
        // this is used after the RideRequest is accepted by a driver.
        // below line is used to check if the previous RideRequest was actually created and present.
       rideRequestRepository.findById(rideRequest.getId())
               .orElseThrow(()-> new RuntimeException("RideRequest not found with id : " +rideRequest.getId()));
       rideRequestRepository.save(rideRequest);
    }
}
