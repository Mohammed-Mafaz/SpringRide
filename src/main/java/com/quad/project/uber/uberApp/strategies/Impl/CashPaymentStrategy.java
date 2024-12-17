package com.quad.project.uber.uberApp.strategies.Impl;

import com.quad.project.uber.uberApp.entities.Driver;
import com.quad.project.uber.uberApp.entities.Payment;
import com.quad.project.uber.uberApp.entities.Wallet;
import com.quad.project.uber.uberApp.enums.PaymentStatus;
import com.quad.project.uber.uberApp.enums.TransactionMethod;
import com.quad.project.uber.uberApp.repository.PaymentRepository;
import com.quad.project.uber.uberApp.service.PaymentService;
import com.quad.project.uber.uberApp.service.WalletService;
import com.quad.project.uber.uberApp.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// CashPayment
// Rider gives 100
// Driver takes 100
// 30Rs is deducted from the Driver's wallet

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null,
                payment.getRide(), TransactionMethod.Ride);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

//  same reason as in wallet. [Cyclic dependency]
//        paymentService.updatePaymentStatus(payment, PaymentStatus.CONFIRMED);

    }

}
