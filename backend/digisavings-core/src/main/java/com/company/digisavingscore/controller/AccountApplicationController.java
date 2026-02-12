package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.service.IAccountApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for Module 2: Account Application management.
 * Handles user requests to apply for a savings account and check status.
 */
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AccountApplicationController {

    private final IAccountApplicationService service;

    /**
     * Endpoint to submit a new account application.
     * @param request Account type and required details.
     * @return 201 Created with the application details.
     */
    @PostMapping("/apply")
    public ResponseEntity<AccountApplicationResponse> apply(@Valid @RequestBody AccountApplicationRequest request) {
        AccountApplicationResponse response = service.applyForAccount(request);
        // Returns 201 Created for a successful submission
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Gets application status for the logged-in user.
     * Extracts user ID from the authentication token for security.
     * @param authentication Current security context.
     * @return Application status and date.
     */
    @GetMapping("/application/status") // Path parameter removed for security
    public ResponseEntity<AccountApplicationResponse> getApplicationStatus(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());

        AccountApplicationResponse response = service.getApplicationStatus(userId);
        return ResponseEntity.ok(response);
    }
}