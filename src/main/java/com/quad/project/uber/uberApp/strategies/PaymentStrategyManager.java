package com.quad.project.uber.uberApp.strategies;

import com.quad.project.uber.uberApp.enums.PaymentMethod;
import com.quad.project.uber.uberApp.strategies.Impl.CashPaymentStrategy;
import com.quad.project.uber.uberApp.strategies.Impl.WalletPaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PaymentStrategyManager {

    private final CashPaymentStrategy cashPaymentStrategy;
    private final WalletPaymentStrategy walletPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod) {
            switch (paymentMethod) {
                case WALLET:
                    return walletPaymentStrategy;
                case CASH:
                    return cashPaymentStrategy;
                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
            }


//        return switch (paymentMethod) {
//            case WALLET -> walletPaymentStrategy;
//            case CASH -> cashPaymentStrategy;
//            default -> throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
//        };
    }

}
