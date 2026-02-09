package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.AccountStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class AccountDetailsResponse {

    private Long accountId;
    private String accountNumber;
    private String accountHolderName;
    private BigDecimal balance;
    private AccountStatus status;
}
