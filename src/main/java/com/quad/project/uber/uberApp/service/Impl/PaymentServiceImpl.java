package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.entities.Payment;
import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.enums.PaymentStatus;
import com.quad.project.uber.uberApp.exception.ResourceNotFoundException;
import com.quad.project.uber.uberApp.repository.PaymentRepository;
import com.quad.project.uber.uberApp.service.PaymentService;
import com.quad.project.uber.uberApp.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyManager paymentStrategyManager;

    @Override
    public void processPayment(Ride ride) {
        // two ways to process the payment , CASH or WALLET
        // chose this by paymentMethod which is present in Payment and which was got from Ride.
        Payment payment = paymentRepository.findByRide(ride)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found for ride with id : " + ride.getId()));
        paymentStrategyManager.paymentStrategy(payment.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                .paymentMethod(ride.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .amount(ride.getFare())
                .ride(ride)
                .build();
        return paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentStatus(Payment payment, PaymentStatus paymentStatus) {
        payment.setPaymentStatus(paymentStatus);
        paymentRepository.save(payment);
    }
}
