package com.company.digisavingscore.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author Karanam Hima Varsha
 */
@Builder
@Data
public class AccountApplicationResponse {
    private Long id;
    private String status;
    private String accountType;
    private LocalDateTime applicationDate;

    private String occupation;
    private String incomeSource;
    private Double annualIncome;
    private String parentName;
    private LocalDate dob;

    private String reviewedBy;
    private LocalDateTime reviewDate;
}