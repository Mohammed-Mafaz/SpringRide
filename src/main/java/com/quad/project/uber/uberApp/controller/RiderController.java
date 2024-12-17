package com.quad.project.uber.uberApp.controller;

import com.quad.project.uber.uberApp.dto.*;
import com.quad.project.uber.uberApp.service.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riders")
@RequiredArgsConstructor
@Secured("ROLE_RIDER")
public class RiderController {

    private final RiderService riderService;

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto){
        return ResponseEntity.ok(riderService.requestRide(rideRequestDto));
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RiderRideDto> cancelRide(@RequestBody Long rideId){
        return ResponseEntity.ok(riderService.cancelRide(rideId));
    }
    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDto> getMyProfile(@RequestBody Long rideId){
        return ResponseEntity.ok(riderService.getMyProfile());
    }

    @PostMapping("/rateDriver")
    public ResponseEntity<DriverDto> rateDriver(@RequestBody RatingDto ratingDto){
        return ResponseEntity.ok(riderService.rateDriver(ratingDto.getRideId(),ratingDto.getRating()));
    }

    @GetMapping("/getAllMyRides")
    public ResponseEntity<Page<RiderRideDto>> getAllMyRides(@RequestParam(defaultValue = "0") Integer pageOffset,
                                                            @RequestParam(defaultValue = "10", required = false) Integer pageSize){
        PageRequest pageRequest = PageRequest.of(pageOffset,pageSize);
        return ResponseEntity.ok(riderService.getAllMyRides(pageRequest));
    }
}
