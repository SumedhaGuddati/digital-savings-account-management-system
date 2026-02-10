package com.company.digisavingscore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminApplicationViewResponse {
    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private LocalDate applicationDate;
    private String status;
    private String reviewedBy;
    private LocalDate reviewDate;
}