package com.company.digisavingscore.mapper;

import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.entity.AccountTransaction;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(AccountTransaction tx, String message) {
        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .accountId(tx.getAccount().getId())
                .transactionType(tx.getTransactionType())
                .amount(tx.getAmount())
                .transactionDate(tx.getTransactionDate())
                .description(tx.getDescription())
                .message(message)
                .build();
    }

    public TransactionHistoryResponse.TransactionItem toHistoryItem(AccountTransaction tx) {
        return TransactionHistoryResponse.TransactionItem.builder()
                .transactionId(tx.getId())
                .transactionType(tx.getTransactionType())
                .amount(tx.getAmount())
                .transactionDate(tx.getTransactionDate())
                .description(tx.getDescription())
                .build();
    }

    public List<TransactionHistoryResponse.TransactionItem> toHistoryItems(List<AccountTransaction> transactions) {
        return transactions.stream()
                .map(this::toHistoryItem)
                .toList();
    }

    public TransactionHistoryResponse toHistoryResponse(Long accountId, List<TransactionHistoryResponse.TransactionItem> items) {
        return TransactionHistoryResponse.builder()
                .accountId(accountId)
                .transactions(items)
                .build();
    }
}
