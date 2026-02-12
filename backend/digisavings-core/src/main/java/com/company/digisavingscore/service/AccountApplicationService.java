package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.entity.AccountApplication;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.ApplicationStatus;
import com.company.digisavingscore.exception.AccountAlreadyExistsException;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.mapper.AccountApplicationMapper;
import com.company.digisavingscore.repository.AccountApplicationRepository;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountApplicationService implements IAccountApplicationService {

    private final AccountApplicationRepository repository;
    private final AccountApplicationMapper mapper;
    private final UserRepository userRepository;

    @Override
    public AccountApplicationResponse applyForAccount(AccountApplicationRequest request) {

        Long userId = getCurrentUserId();

        log.info("Processing account application for user ID: {}", userId);

        if (repository.findByUserId(userId).isPresent()) {
            log.warn("Duplicate application attempt for user ID: {}", userId);
            throw new AccountAlreadyExistsException("A savings account application already exists for this user.");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        AccountApplication entity = new AccountApplication();
        entity.setUser(user);
        entity.setAccountType(request.getAccountType());
        entity.setApplicationDate(LocalDateTime.now());
        entity.setStatus(ApplicationStatus.PENDING);
        entity.setParentName(request.getParentName());
        entity.setOccupation(request.getOccupation());
        entity.setIncomeSource(request.getIncomeSource());
        entity.setAnnualIncome(request.getAnnualIncome());
        entity.setDob(java.time.LocalDate.parse(request.getDob()));
        AccountApplication saved = repository.save(entity);
        log.info("Application saved with ID: {}", saved.getId());

        return mapper.toResponse(saved);
    }

    @Override
    public AccountApplicationResponse getApplicationStatus() {
        Long userId = getCurrentUserId();

        log.info("Retrieving status for user ID: {}", userId);

        AccountApplication application = repository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No application found for user ID: " + userId));

        return mapper.toResponse(application);
    }

    private Long getCurrentUserId() {
        String email = SecurityUtils.getCurrentUserEmail();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found for user email={}", email);
                    return new ResourceNotFoundException("User not found for current user");
                }).getId();
    }
}