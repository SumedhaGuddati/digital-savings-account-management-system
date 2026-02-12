package com.company.digisavingscore.dto.request;

import com.company.digisavingscore.entity.enums.AccountType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountApplicationRequest {

    @NotNull(message = "Account type is required")
    private AccountType accountType;

    @NotBlank(message = "Parent/Guardian name is required")
    private String parentName;

    @NotBlank(message = "Date of birth is required")
    private String dob;

    @NotBlank(message = "Occupation is required")
    private String occupation;

    @NotBlank(message = "Income source is required")
    private String incomeSource;

    @NotNull(message = "Annual income is required")
    @Min(value = 0, message = "Annual income must be a positive value")
    private Double annualIncome;
}