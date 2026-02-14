package com.company.digisavingscore.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Bharadi Harshitha
 */
@Data
@Builder
public class AdminApplicationViewResponse {
    private Long applicationId;
    private String applicantName;
    private String applicantEmail;
    private LocalDateTime applicationDate;
    private String status;
    private String reviewedBy;
    private LocalDateTime reviewDate;
}