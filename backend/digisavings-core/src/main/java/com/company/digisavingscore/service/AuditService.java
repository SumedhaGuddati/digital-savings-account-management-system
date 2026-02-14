package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.AuditEventResponse;
import com.company.digisavingscore.entity.*;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bharadi Harshitha
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AccountApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final AccountTransactionRepository transactionRepository;

    public List<AuditEventResponse> getAuditLogs(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String eventType,
            String severity,
            int page,
            int size) {

        log.info("Fetching audit logs with filters - startDate: {}, endDate: {}, eventType: {}, severity: {}",
                startDate, endDate, eventType, severity);

        List<AuditEventResponse> allEvents = new ArrayList<>();

        // 1. Get Application Events (Approvals/Rejections)
        allEvents.addAll(getApplicationEvents(startDate, endDate));

        // 2. Get User Events (Registrations)
        allEvents.addAll(getUserEvents(startDate, endDate));

        // 3. Get Account Events (Account Creation)
        allEvents.addAll(getAccountEvents(startDate, endDate));

        // 4. Get Transaction Events
        allEvents.addAll(getTransactionEvents(startDate, endDate));

        // Sort by timestamp descending (newest first)
        allEvents.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        // Apply filters
        if (eventType != null && !eventType.isEmpty()) {
            allEvents = allEvents.stream()
                    .filter(e -> e.getAction().equalsIgnoreCase(eventType))
                    .collect(Collectors.toList());
        }

        if (severity != null && !severity.isEmpty()) {
            allEvents = allEvents.stream()
                    .filter(e -> e.getSeverity().equalsIgnoreCase(severity))
                    .collect(Collectors.toList());
        }

        // Apply pagination
        int start = Math.min(page * size, allEvents.size());
        int end = Math.min((page + 1) * size, allEvents.size());

        if (start >= allEvents.size()) {
            return new ArrayList<>();
        }

        return allEvents.subList(start, end);
    }

    public Long getAuditLogsCount(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String eventType,
            String severity) {

        List<AuditEventResponse> allEvents = new ArrayList<>();
        allEvents.addAll(getApplicationEvents(startDate, endDate));
        allEvents.addAll(getUserEvents(startDate, endDate));
        allEvents.addAll(getAccountEvents(startDate, endDate));
        allEvents.addAll(getTransactionEvents(startDate, endDate));

        // Apply filters
        if (eventType != null && !eventType.isEmpty()) {
            allEvents = allEvents.stream()
                    .filter(e -> e.getAction().equalsIgnoreCase(eventType))
                    .collect(Collectors.toList());
        }

        if (severity != null && !severity.isEmpty()) {
            allEvents = allEvents.stream()
                    .filter(e -> e.getSeverity().equalsIgnoreCase(severity))
                    .collect(Collectors.toList());
        }

        return (long) allEvents.size();
    }

    private List<AuditEventResponse> getApplicationEvents(LocalDateTime startDate, LocalDateTime endDate) {
        List<AccountApplication> applications;

        if (startDate != null && endDate != null) {
            applications = applicationRepository.findByReviewDateBetween(startDate, endDate);
        } else {
            applications = applicationRepository.findAllByOrderByReviewDateDesc();
        }

        return applications.stream()
                .filter(app -> app.getStatus() != ApplicationStatus.PENDING && app.getReviewDate() != null)
                .map(app -> {
                    String action = app.getStatus() == ApplicationStatus.APPROVED ?
                            "APPLICATION_APPROVED" : "APPLICATION_REJECTED";
                    String severity = app.getStatus() == ApplicationStatus.APPROVED ? "INFO" : "WARNING";
                    String details = String.format("%s application for %s - Account Type: %s",
                            app.getStatus(),
                            app.getUser().getEmail(),
                            app.getAccountType());

                    return AuditEventResponse.builder()
                            .id(app.getId())
                            .timestamp(app.getReviewDate())
                            .username(app.getReviewedBy())
                            .userEmail(app.getUser().getEmail())
                            .action(action)
                            .entityType("AccountApplication")
                            .entityId(app.getId())
                            .details(details)
                            .severity(severity)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<AuditEventResponse> getUserEvents(LocalDateTime startDate, LocalDateTime endDate) {
        List<User> users;

        if (startDate != null && endDate != null) {
            users = userRepository.findByCreatedAtBetween(startDate, endDate);
        } else {
            users = userRepository.findAllByOrderByCreatedAtDesc();
        }

        return users.stream()
                .map(user -> AuditEventResponse.builder()
                        .id(user.getId())
                        .timestamp(user.getCreatedAt())
                        .username(user.getEmail())
                        .userEmail(user.getEmail())
                        .action("USER_REGISTERED")
                        .entityType("User")
                        .entityId(user.getId())
                        .details(String.format("New user registered: %s (%s) - Role: %s",
                                user.getName(), user.getEmail(), user.getRole()))
                        .severity("INFO")
                        .build())
                .collect(Collectors.toList());
    }

    private List<AuditEventResponse> getAccountEvents(LocalDateTime startDate, LocalDateTime endDate) {
        List<SavingsAccount> accounts;

        if (startDate != null && endDate != null) {
            accounts = savingsAccountRepository.findByOpenedDateBetween(
                    startDate.toLocalDate(),
                    endDate.toLocalDate());
        } else {
            accounts = savingsAccountRepository.findAllByOrderByOpenedDateDesc();
        }

        return accounts.stream()
                .map(account -> AuditEventResponse.builder()
                        .id(account.getId())
                        .timestamp(account.getOpenedDate().atStartOfDay())
                        .username(account.getUser().getEmail())
                        .userEmail(account.getUser().getEmail())
                        .action("ACCOUNT_CREATED")
                        .entityType("SavingsAccount")
                        .entityId(account.getId())
                        .details(String.format("Savings account created: %s - Balance: $%s, Interest Rate: %s%%",
                                account.getAccountNumber(),
                                account.getBalance(),
                                account.getInterestRate()))
                        .severity("INFO")
                        .build())
                .collect(Collectors.toList());
    }

    private List<AuditEventResponse> getTransactionEvents(LocalDateTime startDate, LocalDateTime endDate) {
        List<AccountTransaction> transactions;

        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByTransactionDateBetween(startDate, endDate);
        } else {
            transactions = transactionRepository.findAllByOrderByTransactionDateDesc();
        }

        return transactions.stream()
                .map(tx -> {
                    String severity = tx.getAmount().compareTo(new BigDecimal("10000")) > 0 ?
                            "WARNING" : "INFO";
                    String action = "TRANSACTION_" + tx.getTransactionType();

                    return AuditEventResponse.builder()
                            .id(tx.getId())
                            .timestamp(tx.getTransactionDate())
                            .username(tx.getAccount().getUser().getEmail())
                            .userEmail(tx.getAccount().getUser().getEmail())
                            .action(action)
                            .entityType("AccountTransaction")
                            .entityId(tx.getId())
                            .details(String.format("%s: $%s - Account: %s - %s",
                                    tx.getTransactionType(),
                                    tx.getAmount(),
                                    tx.getAccount().getAccountNumber(),
                                    tx.getDescription()))
                            .severity(severity)
                            .build();
                })
                .collect(Collectors.toList());
    }

    public List<String> getEventTypes() {
        return List.of(
                "APPLICATION_APPROVED",
                "APPLICATION_REJECTED",
                "USER_REGISTERED",
                "ACCOUNT_CREATED",
                "TRANSACTION_DEPOSIT",
                "TRANSACTION_WITHDRAWAL",
                "TRANSACTION_TRANSFER"
        );
    }
}