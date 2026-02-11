package com.company.digisavingscore.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Data
public class AccountApplicationResponse {
    private Long id;
    private String accountType;
    private LocalDateTime applicationDate;
    private String status;
}