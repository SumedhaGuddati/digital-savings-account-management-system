package com.company.digisavingscore.service.impl;


import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public interface IAdminAccountService {
    List<AdminApplicationViewResponse> getPendingApplications();


    SavingsAccountResponse approveApplication(Long applicationId,
                                              AccountApprovalRequest request,
                                              String adminUsername);


    SavingsAccountResponse rejectApplication(Long applicationId,
                                             AccountApprovalRequest request,
                                             String adminUsername);


    SavingsAccountResponse processApplication(Long applicationId,
                                              AccountApprovalRequest request,
                                              String adminUsername);
}