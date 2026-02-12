package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.AccountType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserProfileDetailsResponse {
    private Long accountId;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType accountType;
    private LocalDate dob;
    private LocalDateTime applicationDate;
    private LocalDateTime reviewDate;
    private String occupation;
    private String incomeSource;
    private Double annualIncome;
    private String parentName;
    private Long transactionsCount;
}
