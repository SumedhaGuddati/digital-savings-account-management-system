package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;

public interface IAccountApplicationService {
    AccountApplicationResponse applyForAccount(AccountApplicationRequest request);
    AccountApplicationResponse getApplicationStatus(Long userId);
}