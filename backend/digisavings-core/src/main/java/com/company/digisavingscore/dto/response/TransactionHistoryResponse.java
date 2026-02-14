package com.company.digisavingscore.dto.response;

import com.company.digisavingscore.entity.enums.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Khamitkar Sai Chandan
 */
@Data
@AllArgsConstructor
@Builder
public class TransactionHistoryResponse {

    private Long accountId;
    private List<TransactionItem> transactions;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TransactionItem {
        private Long transactionId;
        private TransactionType transactionType;
        private BigDecimal amount;
        private LocalDateTime transactionDate;
        private String description;
    }
}
