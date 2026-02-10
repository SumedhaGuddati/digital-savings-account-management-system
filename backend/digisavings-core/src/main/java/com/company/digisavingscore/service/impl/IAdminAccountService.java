package com.company.digisavingscore.service.impl;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.*;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.entity.enums.AccountStatus;
import java.util.List;

public interface IAdminAccountService {
    List<AdminApplicationViewResponse> getPendingApplications();
    List<AdminApplicationViewResponse> getAllApplications(ApplicationStatus status);
    AdminApplicationViewResponse getApplicationDetails(Long applicationId);
    SavingsAccountResponse approveApplication(Long applicationId, AccountApprovalRequest request, String adminUsername);
    SavingsAccountResponse rejectApplication(Long applicationId, AccountApprovalRequest request, String adminUsername);
    SavingsAccountResponse processApplication(Long applicationId, AccountApprovalRequest request, String adminUsername);
    List<SavingsAccountResponse> getAllSavingsAccounts(AccountStatus status);
    SavingsAccountResponse getAccountDetails(Long accountId);
    SavingsAccountResponse updateAccountStatus(Long accountId, AccountStatus newStatus, String adminUsername);
}