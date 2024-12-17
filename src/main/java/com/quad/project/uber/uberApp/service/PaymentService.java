package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.entities.Payment;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus);
}
