package com.quad.project.uber.uberApp.strategies;

import com.quad.project.uber.uberApp.strategies.Impl.DriverMatchingHighestRatedDriverStrategy;
import com.quad.project.uber.uberApp.strategies.Impl.DriverMatchingNearestDriverStrategy;
import com.quad.project.uber.uberApp.strategies.Impl.RideFareDefaultFareCalculationStrategy;
import com.quad.project.uber.uberApp.strategies.Impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingNearestDriverStrategy nearestDriverStrategy;
    private final DriverMatchingHighestRatedDriverStrategy highestRatedDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy defaultFareCalculationStrategy;;
    private final RideFareSurgePricingFareCalculationStrategy surgePricingFareCalculationStrategy;

    public DriverMatchingStrategy driverMatchingStrategy(double rating){
        if(rating >= 4.8){
            return highestRatedDriverStrategy;
        }else {
            return nearestDriverStrategy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        // peak time : 6PM to 9PM
        // try to get local time from the rider's phone ; step : get the zoneId from the user's device
        LocalTime surgeStartTime = LocalTime.of(18,0);
        LocalTime surgeEndTime = LocalTime.of(21,0);
        LocalTime currentTime = LocalTime.now();
        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if(isSurgeTime){
            return surgePricingFareCalculationStrategy;
        }else{
            return defaultFareCalculationStrategy;
        }

    }
}
