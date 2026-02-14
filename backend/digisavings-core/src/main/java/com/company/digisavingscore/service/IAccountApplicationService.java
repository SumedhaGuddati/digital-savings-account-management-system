package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;

/**
 * @author Karanam Hima Varsha
 */
public interface IAccountApplicationService {
    AccountApplicationResponse applyForAccount(AccountApplicationRequest request);
    AccountApplicationResponse getApplicationStatus();
}