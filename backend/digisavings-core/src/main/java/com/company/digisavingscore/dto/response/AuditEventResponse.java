package com.company.digisavingscore.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Bharadi Harshitha
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditEventResponse {
    private Long id;
    private LocalDateTime timestamp;
    private String username;
    private String userEmail;
    private String action;
    private String entityType;
    private Long entityId;
    private String details;
    private String severity;
    private String ipAddress;
}