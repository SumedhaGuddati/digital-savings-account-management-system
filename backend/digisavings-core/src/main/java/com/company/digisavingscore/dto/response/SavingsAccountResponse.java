package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class SavingsAccountResponse {
    private Long id;
    private String accountNumber;
    private Long userId;
    private String userName;
    private String userEmail;
    private BigDecimal balance;
    private AccountStatus status;
    private BigDecimal interestRate;
    private LocalDate openedDate;
    private String message;
}