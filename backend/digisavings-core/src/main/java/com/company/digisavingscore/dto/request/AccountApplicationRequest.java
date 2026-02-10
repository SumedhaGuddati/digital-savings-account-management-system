package com.company.digisavingscore.dto.request;

import com.company.digisavingscore.entity.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountApplicationRequest {
    @NotBlank(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account type cannot be blank")
    private AccountType accountType; // Should be "SAVINGS"
}