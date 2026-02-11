package com.company.digisavingscore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AccountApprovalRequest {
    private String decision;
    private String reviewerComments;
}