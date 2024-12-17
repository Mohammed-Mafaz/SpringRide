package com.quad.project.uber.uberApp.repository;

import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.User;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

    // ST_Distance(point1, point2) --> gives the distance btw point1 & point2
    // ST_DWithin(point1, point2, value) --> return boolean. checks whether p1 & p2 are within the value_distance.

    // Select all drivers and distance btw drivers and rider
    // where driver are available and the distance is less than 10km (uses B-Tree or something which indexes)
    // postgis indexes the given point then from there a page is picked and searched
    // this only take O(log n) time. [so the query is optimized].
    @Query(value = "SELECT d.*, ST_Distance(d.current_location, :pickupLocation) AS distance " +
            "FROM driver d " +
            "WHERE available = true AND ST_DWithin(d.current_location, :pickupLocation, 10000) " +
            "ORDER BY distance " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenMatchingDrivers(Point pickupLocation);

    @Query(value = "Select d.* " +
            "FROM driver d " +
            "WHERE available=true AND ST_DWithin(d.current_location, :pickupLocation, 15000) " +
            "ORDER BY d.rating DESC " +
            "LIMIT 10", nativeQuery = true)
    List<Driver> findTenNearbyTopRatedDrivers(Point pickupLocation);

    Optional<Driver> findByUser(User user);

    // NOTE : here when used postgis method to find the 10 drivers using *Manhattan distance*
    // but while calculating the fare , where using OSRM api to find the actual road distance.
}
