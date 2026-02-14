package com.company.digisavingscore.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Khamitkar Sai Chandan
 */
@Data
public class TransactionRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    private String description;
}
