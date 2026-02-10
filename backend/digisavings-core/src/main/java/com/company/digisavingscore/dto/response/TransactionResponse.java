package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.TransactionType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionResponse {

    private Long transactionId;
    private Long accountId;
    private TransactionType transactionType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
    private String message;
}
