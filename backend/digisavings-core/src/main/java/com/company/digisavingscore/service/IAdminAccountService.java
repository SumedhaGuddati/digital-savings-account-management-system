package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.dto.response.AdminDashboardStatsResponse;

import java.util.List;

public interface IAdminAccountService {
    List<AdminApplicationViewResponse> getPendingApplications();
    List<AdminApplicationViewResponse> getApprovedApplications();
    SavingsAccountResponse approveApplication(Long applicationId, AccountApprovalRequest request, String adminUsername);
    SavingsAccountResponse rejectApplication(Long applicationId, AccountApprovalRequest request, String adminUsername);
}