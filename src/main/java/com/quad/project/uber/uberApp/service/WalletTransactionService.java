package com.quad.project.uber.uberApp.service;

import com.quad.project.uber.uberApp.dto.WalletTransactionDto;
import com.quad.project.uber.uberApp.entities.WalletTransaction;

public interface WalletTransactionService {

    void createNewWalletTransaction(WalletTransaction walletTransaction);
}
