package com.company.digisavingscore.mapper;

import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.entity.AccountApplication;
import org.springframework.stereotype.Component;

@Component
public class AccountApplicationMapper {

    public AccountApplicationResponse toResponse(AccountApplication entity) {
        if (entity == null) {
            return null;
        }

        return AccountApplicationResponse.builder()
                .id(entity.getId())
                .accountType(entity.getAccountType().name())
                .applicationDate(entity.getApplicationDate())
                .status(entity.getStatus().name())
                .build();
    }
}