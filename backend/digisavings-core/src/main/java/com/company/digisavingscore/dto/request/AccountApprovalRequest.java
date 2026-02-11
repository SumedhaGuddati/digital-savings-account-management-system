package com.company.digisavingscore.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class AccountApprovalRequest {

    @NotBlank
    private String decision;

    @NotBlank
    private String reviewerComments;
}