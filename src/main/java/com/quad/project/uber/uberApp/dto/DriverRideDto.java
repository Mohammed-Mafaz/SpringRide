package com.quad.project.uber.uberApp.dto;

import com.quad.project.uber.uberApp.enums.PaymentMethod;
import com.quad.project.uber.uberApp.enums.RideRequestStatus;
import com.quad.project.uber.uberApp.enums.RideStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DriverRideDto {

    private Long id;

    private PointDto pickupLocation;
    private PointDto dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;
    private DriverDto driver;

    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;


    private Double fare;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
}
