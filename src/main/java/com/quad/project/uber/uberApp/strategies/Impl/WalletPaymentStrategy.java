package com.quad.project.uber.uberApp.strategies.Impl;

import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.Payment;
import com.quad.project.uber.uberApp.entities.Rider;
import com.quad.project.uber.uberApp.enums.PaymentStatus;
import com.quad.project.uber.uberApp.enums.TransactionMethod;
import com.quad.project.uber.uberApp.repository.PaymentRepository;
import com.quad.project.uber.uberApp.service.PaymentService;
import com.quad.project.uber.uberApp.service.WalletService;
import com.quad.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Rider had 232, Driver had 500
// Ride cost is 100, commission=30
// Rider -> 232-100 = 132
// Driver -> 500 + (100 - 30) = 570

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null,
                payment.getRide(), TransactionMethod.Ride);

        double driversCut = payment.getAmount() * (1-PLATFORM_COMMISSION);
        // 100 * (1- 0.3) = 70

        walletService.addMoneyToWallet(driver.getUser(), driversCut,
                null,payment.getRide(),TransactionMethod.Ride);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);

        paymentRepository.save(payment);

          // can't use the below line due to cyclic dependency
//        paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);

    }
}
