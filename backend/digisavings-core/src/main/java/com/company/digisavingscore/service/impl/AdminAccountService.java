package com.company.digisavingscore.service.impl;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import com.company.digisavingscore.service.IAdminAccountService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService implements IAdminAccountService {

    private static final Logger logger = LogManager.getLogger(AdminAccountServiceImpl.class);

    private final AccountApplicationRepository applicationRepository;
    private final SavingsAccountRepository savingsAccountRepository;

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("4.0");
    private static final String ACCOUNT_NUMBER_PREFIX = "SAV";

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationViewResponse> getPendingApplications() {
        logger.info("Fetching all pending account applications");
        return applicationRepository.findByStatus(ApplicationStatus.PENDING)
                .stream()
                .map(app -> AdminApplicationViewResponse.builder()
                        .applicationId(app.getId())
                        .applicantName(app.getUser().getName())
                        .applicantEmail(app.getUser().getEmail())
                        .status(app.getStatus().name())
                        .applicationDate(app.getApplicationDate())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public SavingsAccountResponse approveApplication(Long applicationId,
                                                     AccountApprovalRequest request,
                                                     String adminUsername) {
        logger.info("Approving application with ID: {}", applicationId);

        AccountApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("Application with ID {} not found", applicationId);
                    return new ResourceNotFoundException("Application not found");
                });

        if (application.getStatus() != ApplicationStatus.PENDING) {
            logger.warn("Application ID {} already processed with status {}", applicationId, application.getStatus());
            throw new IllegalStateException("Application already processed");
        }

        User user = application.getUser();
        if (savingsAccountRepository.findByUser(user).isPresent()) {
            logger.warn("User {} already has a savings account", user.getId());
            throw new IllegalStateException("User already has a savings account");
        }

        application.setStatus(ApplicationStatus.APPROVED);
        application.setProcessedBy(adminUsername);
        application.setProcessedDate(LocalDateTime.now());
        applicationRepository.save(application);

        SavingsAccount account = SavingsAccount.builder()
                .accountNumber(ACCOUNT_NUMBER_PREFIX + String.format("%010d", application.getId()))
                .user(user)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .interestRate(DEFAULT_INTEREST_RATE)
                .openedDate(LocalDate.now())
                .accountApplication(application)
                .createdBy(adminUsername)
                .createdDate(LocalDateTime.now())
                .minimumBalance(new BigDecimal("100.00"))
                .isPrimary(true)
                .build();

        savingsAccountRepository.save(account);
        logger.info("Savings account created for user {} with account number {}", user.getId(), account.getAccountNumber());

        return SavingsAccountResponse.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .balance(account.getBalance())
                .status(account.getStatus())
                .interestRate(account.getInterestRate())
                .openedDate(account.getOpenedDate())
                .message("Account approved and created successfully")
                .processedDate(LocalDateTime.now())
                .build();
    }

    @Override
    public SavingsAccountResponse rejectApplication(Long applicationId,
                                                    AccountApprovalRequest request,
                                                    String adminUsername) {
        logger.info("Rejecting application with ID: {}", applicationId);

        AccountApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    logger.error("Application with ID {} not found", applicationId);
                    return new ResourceNotFoundException("Application not found");
                });

        if (application.getStatus() != ApplicationStatus.PENDING) {
            logger.warn("Application ID {} already processed with status {}", applicationId, application.getStatus());
            throw new IllegalStateException("Application already processed");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setProcessedBy(adminUsername);
        application.setProcessedDate(LocalDateTime.now());
        applicationRepository.save(application);

        logger.info("Application ID {} rejected by admin {}", applicationId, adminUsername);

        return SavingsAccountResponse.builder()
                .applicationId(application.getId())
                .applicantName(application.getUser().getName())
                .applicantEmail(application.getUser().getEmail())
                .status(ApplicationStatus.REJECTED.name())
                .message("Application rejected")
                .processedBy(adminUsername)
                .processedDate(LocalDateTime.now())
                .rejectionReason(request.getNotes())
                .build();
    }

    @Override
    public SavingsAccountResponse processApplication(Long applicationId,
                                                     AccountApprovalRequest request,
                                                     String adminUsername) {
        String decision = request.getDecision() == null ? "" : request.getDecision().trim().toUpperCase();
        logger.info("Processing application ID {} with decision '{}'", applicationId, decision);

        return switch (decision) {
            case "APPROVE" -> approveApplication(applicationId, request, adminUsername);
            case "REJECT" -> rejectApplication(applicationId, request, adminUsername);
            default -> {
                logger.error("Invalid decision '{}' for application ID {}", decision, applicationId);
                throw new IllegalArgumentException("Decision must be APPROVE or REJECT");
            }
        };
    }
}