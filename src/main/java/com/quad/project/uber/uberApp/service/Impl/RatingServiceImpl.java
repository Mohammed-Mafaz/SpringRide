package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.dto.DriverDto;
import com.quad.project.uber.uberApp.dto.RiderDto;
import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.Rating;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.entities.Rider;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.exception.RuntimeConflictException;
import com.quad.project.uber.uberApp.repository.DriverRepository;
import com.quad.project.uber.uberApp.repository.RatingRepository;
import com.quad.project.uber.uberApp.repository.RiderRepository;
import com.quad.project.uber.uberApp.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final DriverRepository driverRepository;
    private final RiderRepository riderRepository;
    private final ModelMapper modelMapper;

    @Override
    public DriverDto rateDriver(Ride ride, Integer rating) {
        // 1. get the ratingObject which was created when Ride was Started.
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(()-> new ResourceNotFoundException("ratingObj not found for ride with id: "+ ride.getId()));

        // 1.1 check if the ratingObject has already been rated previously
        if(ratingObj.getDriverRating() != null) throw new RuntimeConflictException("Driver has already been rated, cannot be rated again.");

        // 2. set the rating value into this ratingObject
        ratingObj.setDriverRating(rating);

        // 3. save this ratingObject
        ratingRepository.save(ratingObj);

        // :: calculate the avg rating of the driver
        Driver driver = ride.getDriver();
        Double newRating = ratingRepository.findByDriver(driver)
                .stream()
                .mapToDouble(Rating::getDriverRating)
                .average().orElse(0.0);

        //  update the newRating to Driver & save the Driver
        driver.setRating(newRating);
        Driver savedDriver = driverRepository.save(driver);
        return modelMapper.map(savedDriver,DriverDto.class);
    }

    @Override
    public RiderDto rateRider(Ride ride, Integer rating) {
        Rating ratingObj = ratingRepository.findByRide(ride)
                .orElseThrow(()-> new ResourceNotFoundException("ratingObj not found for ride with id: "+ ride.getId()));
        if(ratingObj.getDriverRating() != null) throw new RuntimeConflictException("Rider has already been rated, cannot be rated again.");
        ratingObj.setRiderRating(rating);
        ratingRepository.save(ratingObj);


        Rider rider = ride.getRider();
        Double newRating = ratingRepository.findByRider(rider)
                .stream()
                .mapToDouble(Rating::getRiderRating)
                .average().orElse(0.0);

        rider.setRating(newRating);
        Rider savedRide = riderRepository.save(rider);
        return modelMapper.map(savedRide,RiderDto.class);
    }

    @Override
    public void createNewRating(Ride ride) {
        Rating rating = Rating.builder()
                .ride(ride)
                .rider(ride.getRider())
                .driver(ride.getDriver())
                .build();
    }
}
