package com.company.digisavingscore.dto.request;

import com.company.digisavingscore.entity.enums.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountApplicationRequest {
    @NotBlank(message = "Account type cannot be blank")
    private AccountType accountType;
}