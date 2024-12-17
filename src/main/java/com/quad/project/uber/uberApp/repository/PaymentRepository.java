package com.quad.project.uber.uberApp.repository;

import com.quad.project.uber.uberApp.entities.Payment;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.entities.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByRide(Ride ride);
}
