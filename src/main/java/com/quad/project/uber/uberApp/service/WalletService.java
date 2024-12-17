package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.entities.*;
import com.quad.project.uber.uberApp.enums.TransactionMethod;

public interface WalletService {

    Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    Wallet deductMoneyFromWallet(User user,Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod);

    void withdrawAllMoneyFromWallet();

    Wallet findWalletById(Long walletId);

    Wallet createNewWallet(User user);

    Wallet findByUser(User user);
}
