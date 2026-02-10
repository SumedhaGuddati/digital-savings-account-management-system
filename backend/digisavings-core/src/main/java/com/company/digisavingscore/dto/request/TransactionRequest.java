package com.company.digisavingscore.dto.request;

import com.company.digisavingscore.entity.enums.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequest {

    @NotNull
    private Long accountId;

    @NotBlank
    private BigDecimal amount;

    private String description;
}
