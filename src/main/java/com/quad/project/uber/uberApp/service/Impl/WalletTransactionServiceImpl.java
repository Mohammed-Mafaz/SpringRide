package com.quad.project.uber.uberApp.service.Impl;

import com.quad.project.uber.uberApp.dto.WalletTransactionDto;
import com.quad.project.uber.uberApp.entities.WalletTransaction;
import com.quad.project.uber.uberApp.repository.WalletTransactionRepository;
import com.quad.project.uber.uberApp.service.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransaction(WalletTransaction walletTransaction) {
        // get the walletTransactionDTO from the USER
//        WalletTransaction walletTransaction = modelMapper.map(walletTransactionDto, WalletTransaction.class);
        walletTransactionRepository.save(walletTransaction);
    }
}
