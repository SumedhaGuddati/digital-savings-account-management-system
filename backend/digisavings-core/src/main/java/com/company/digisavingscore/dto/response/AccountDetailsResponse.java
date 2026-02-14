package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.AccountType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Khamitkar Sai Chandan
 */
@Data
@Builder
public class AccountDetailsResponse {

    private Long accountId;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private AccountStatus status;
    private AccountType accountType;
}
