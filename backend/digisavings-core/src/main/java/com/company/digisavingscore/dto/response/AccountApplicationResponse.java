package com.company.digisavingscore.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AccountApplicationResponse {
    private Long id;
    private String accountType;
    private LocalDateTime applicationDate;
    private String status;
}