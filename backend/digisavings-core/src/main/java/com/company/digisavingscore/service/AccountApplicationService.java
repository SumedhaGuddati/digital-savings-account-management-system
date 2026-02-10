package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.mapper.AccountApplicationMapper;
import com.company.digisavingscore.exception.AccountAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountApplicationService {

    private final AccountApplicationRepository repository;
    private final AccountApplicationMapper mapper;

    public AccountApplicationResponse applyForAccount(AccountApplicationRequest request) {
        log.info("Processing account application for user ID: {}", request.getUserId());

        if (repository.findByUserId(request.getUserId()).isPresent()) {
            log.warn("Application failed: User ID {} already has an existing application", request.getUserId());
            throw new AccountAlreadyExistsException("A savings account application already exists for this user.");
        }

        AccountApplication entity = new AccountApplication();
        entity.setUserId(request.getUserId());
        entity.setAccountType(request.getAccountType());
        entity.setApplicationDate(LocalDate.now());
        entity.setStatus(ApplicationStatus.PENDING);

        AccountApplication saved = repository.save(entity);
        log.info("Successfully created application ID: {} for user ID: {}", saved.getId(), request.getUserId());

        return mapper.toResponse(saved);
    }

    public AccountApplicationResponse getApplicationStatus(Long userId) {
        log.info("Fetching application status for user ID: {}", userId);
        AccountApplication application = repository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Status fetch failed: No application found for user ID: {}", userId);
                    return new ResourceNotFoundException("No account application found for user ID: " + userId);
                });

        return mapper.toResponse(application);
    }
}