package com.company.digisavingscore.service.impl;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.service.IAdminAccountService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService implements IAdminAccountService {

    private static final Logger log = Logger.getLogger(AdminAccountService.class);

    private final AccountApplicationRepository applicationRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationViewResponse> getPendingApplications() {
        return getAllApplications(ApplicationStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationViewResponse> getAllApplications(ApplicationStatus status) {
        List<AccountApplication> apps = (status != null) ?
                applicationRepository.findByStatus(status) : applicationRepository.findAll();
        return apps.stream().map(this::mapToViewResponse).collect(Collectors.toList());
    }

    @Override
    public SavingsAccountResponse approveApplication(Long applicationId, AccountApprovalRequest request, String admin) {
        AccountApplication app = findPendingApp(applicationId);

        if (savingsAccountRepository.existsByUser(app.getUser())) {
            throw new IllegalStateException("User already has an account");
        }

        app.setStatus(ApplicationStatus.APPROVED);
        app.setReviewedBy(admin);
        app.setReviewDate(LocalDate.now());

        SavingsAccount account = SavingsAccount.builder()
                .accountNumber("SAV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(app.getUser())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .openedDate(LocalDate.now())
                .accountApplication(app)
                .createdBy(admin)
                .createdDate(LocalDateTime.now())
                .build();

        SavingsAccount savedAccount = savingsAccountRepository.save(account);
        log.info("Account created successfully: " + savedAccount.getAccountNumber());

        return mapToAccountResponse(savedAccount, "Account successfully created");
    }

    @Override
    public SavingsAccountResponse rejectApplication(Long id, AccountApprovalRequest req, String admin) {
        AccountApplication app = findPendingApp(id);
        app.setStatus(ApplicationStatus.REJECTED);
        app.setReviewedBy(admin);
        app.setReviewDate(LocalDate.now());
        applicationRepository.save(app);

        log.info("Application rejected: " + id + " by " + admin);

        return SavingsAccountResponse.builder()
                .status(AccountStatus.CLOSED)
                .message("Application Rejected by " + admin)
                .build();
    }

    @Override
    public SavingsAccountResponse processApplication(Long id, AccountApprovalRequest req, String admin) {
        String decision = req.getDecision().toUpperCase();
        return switch (decision) {
            case "APPROVE" -> approveApplication(id, req, admin);
            case "REJECT" -> rejectApplication(id, req, admin);
            default -> throw new IllegalArgumentException("Invalid Decision: " + decision);
        };
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavingsAccountResponse> getAllSavingsAccounts(AccountStatus status) {
        List<SavingsAccount> accounts = (status != null) ?
                savingsAccountRepository.findByStatus(status) : savingsAccountRepository.findAll();
        return accounts.stream().map(acc -> mapToAccountResponse(acc, null)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SavingsAccountResponse getAccountDetails(Long id) {
        SavingsAccount acc = savingsAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account ID " + id + " not found"));
        return mapToAccountResponse(acc, null);
    }

    @Override
    public SavingsAccountResponse updateAccountStatus(Long id, AccountStatus status, String admin) {
        SavingsAccount acc = savingsAccountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account Not Found"));
        acc.setStatus(status);
        acc.setUpdatedDate(LocalDateTime.now());
        SavingsAccount saved = savingsAccountRepository.save(acc);
        log.info("Updated account status for " + id + " to " + status);
        return mapToAccountResponse(saved, "Status updated to " + status);
    }

    private AccountApplication findPendingApp(Long id) {
        AccountApplication app = applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application ID " + id + " not found"));
        if (app.getStatus() != ApplicationStatus.PENDING) {
            throw new IllegalStateException("Application is already " + app.getStatus());
        }
        return app;
    }

    private AdminApplicationViewResponse mapToViewResponse(AccountApplication app) {
        return AdminApplicationViewResponse.builder()
                .applicationId(app.getId())
                .applicantName(app.getUser().getName())
                .status(app.getStatus().name())
                .applicationDate(app.getApplicationDate())
                .build();
    }

    private SavingsAccountResponse mapToAccountResponse(SavingsAccount acc, String msg) {
        return SavingsAccountResponse.builder()
                .id(acc.getId())
                .accountNumber(acc.getAccountNumber())
                .userId(acc.getUser().getId())
                .userName(acc.getUser().getName())
                .userEmail(acc.getUser().getEmail())
                .balance(acc.getBalance())
                .status(acc.getStatus())
                .openedDate(acc.getOpenedDate())
                .interestRate(acc.getInterestRate())
                .message(msg)
                .build();
    }
}