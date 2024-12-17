package com.quad.project.uber.uberApp.dto;

import com.quad.project.uber.uberApp.enums.PaymentMethod;
import com.quad.project.uber.uberApp.enums.RideRequestStatus;
import com.quad.project.uber.uberApp.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {

    private Long id;

    private PointDto pickupLocation;
    private PointDto dropOffLocation;

    private LocalDateTime requestedTime;

    private RiderDto rider;

    private PaymentMethod paymentMethod;

    private RideRequestStatus rideRequestStatus;

    private Double fare;
}
