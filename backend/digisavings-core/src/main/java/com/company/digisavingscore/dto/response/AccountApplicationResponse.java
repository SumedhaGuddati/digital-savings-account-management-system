package com.company.digisavingscore.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AccountApplicationResponse {
    private Long id;
    private String accountType;
    private LocalDate applicationDate;
    private String status;
}