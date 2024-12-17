package com.quad.project.uber.uberApp.repository;

import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.RideRequest;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest,Long> {
}
