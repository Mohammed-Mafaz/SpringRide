package com.quad.project.uber.uberApp.dto;

import com.quad.project.uber.uberApp.entities.Ride;
import com.quad.project.uber.uberApp.entities.Wallet;
import com.quad.project.uber.uberApp.enums.Role;
import com.quad.project.uber.uberApp.enums.TransactionMethod;
import com.quad.project.uber.uberApp.enums.TransactionType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private RiderRideDto ride;

    private String transactionId;

    private Wallet wallet;

    private LocalDateTime timeStamp;
}
