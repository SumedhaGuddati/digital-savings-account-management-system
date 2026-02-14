package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.SavingsAccount;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.AccountStatus;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.exception.BusinessRuleViolationException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.AdminAccountMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.SavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Bharadi Harshitha
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService implements IAdminAccountService {

    private final AccountApplicationRepository applicationRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AdminAccountMapper adminAccountMapper;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationViewResponse> getApprovedApplications() {
        log.info("Fetching approved applications");

        List<AccountApplication> applications =
                applicationRepository.findAllByStatusWithUser(ApplicationStatus.APPROVED);

        return applications.stream()
                .map(adminAccountMapper::toAdminApplicationView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdminApplicationViewResponse> getPendingApplications() {
        log.info("Fetching pending applications");

        List<AccountApplication> applications =
                applicationRepository.findByStatus(ApplicationStatus.PENDING);

        return applications.stream()
                .map(adminAccountMapper::toAdminApplicationView)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SavingsAccountResponse approveApplication(
            Long applicationId,
            AccountApprovalRequest request,
            String adminUsername
    ) {

        AccountApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application with ID " + applicationId + " not found"
                        )
                );

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleViolationException(
                    "Application is already " + application.getStatus()
            );
        }

        User user = application.getUser();
        if (user == null) {
            throw new BusinessRuleViolationException("Application has no associated user");
        }

        if (savingsAccountRepository.existsByUser(user)) {
            throw new BusinessRuleViolationException(
                    "User " + user.getEmail() + " already has a savings account"
            );
        }

        application.setStatus(ApplicationStatus.APPROVED);
        application.setReviewedBy(adminUsername);
        application.setReviewDate(LocalDateTime.now());
        applicationRepository.save(application);

        SavingsAccount account = SavingsAccount.builder()
                .accountNumber("SAV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .user(user)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .interestRate(BigDecimal.valueOf(3.5))
                .openedDate(LocalDate.now())
                .build();

        SavingsAccount savedAccount = savingsAccountRepository.save(account);

        emailService.sendApplicationApprovalEmail(
                user.getEmail(),
                user.getName(),
                savedAccount.getAccountNumber()
        );

        return SavingsAccountResponse.builder()
                .id(savedAccount.getId())
                .accountNumber(savedAccount.getAccountNumber())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .balance(savedAccount.getBalance())
                .status(savedAccount.getStatus())
                .interestRate(savedAccount.getInterestRate())
                .openedDate(savedAccount.getOpenedDate())
                .message("Account approved successfully by " + adminUsername)
                .build();
    }

    @Override
    @Transactional
    public SavingsAccountResponse rejectApplication(
            Long applicationId,
            AccountApprovalRequest request,
            String adminUsername
    ) {

        AccountApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Application with ID " + applicationId + " not found"
                        )
                );

        if (application.getStatus() != ApplicationStatus.PENDING) {
            throw new BusinessRuleViolationException(
                    "Application is already " + application.getStatus()
            );
        }

        User user = application.getUser();
        if (user == null) {
            throw new BusinessRuleViolationException("Application has no associated user");
        }

        application.setStatus(ApplicationStatus.REJECTED);
        application.setReviewedBy(adminUsername);
        application.setReviewDate(LocalDateTime.now());
        applicationRepository.save(application);

        emailService.sendApplicationRejectionEmail(
                user.getEmail(),
                user.getName()
        );

        return SavingsAccountResponse.builder()
                .id(null)
                .accountNumber(null)
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.CLOSED)
                .interestRate(BigDecimal.ZERO)
                .openedDate(null)
                .message("Application rejected by " + adminUsername)
                .build();
    }
}