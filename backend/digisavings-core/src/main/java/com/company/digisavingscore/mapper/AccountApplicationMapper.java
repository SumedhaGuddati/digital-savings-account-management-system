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

        AccountApplicationResponse response = new AccountApplicationResponse();
        response.setId(entity.getId());
        response.setAccountType(entity.getAccountType());
        response.setApplicationDate(entity.getApplicationDate());
        response.setStatus(entity.getStatus().name());

        return response;
    }
}