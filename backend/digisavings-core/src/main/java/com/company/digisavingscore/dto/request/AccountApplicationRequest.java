package com.company.digisavingscore.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountApplicationRequest {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account type cannot be blank")
    private AccountType accountType; // Should be "SAVINGS"
}