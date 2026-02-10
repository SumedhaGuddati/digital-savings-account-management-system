package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApplicationRequest;
import com.company.digisavingscore.dto.response.AccountApplicationResponse;
import com.company.digisavingscore.service.AccountApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountApplicationController {

    private final IAccountApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<AccountApplicationResponse> apply(@Valid @RequestBody AccountApplicationRequest request) {
        AccountApplicationResponse response = service.applyForAccount(request);
        // Returns 201 Created for a successful submission
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/application/status") // Path parameter removed for security
    public ResponseEntity<AccountApplicationResponse> getApplicationStatus(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());

        AccountApplicationResponse response = service.getApplicationStatus(userId);
        return ResponseEntity.ok(response);
    }
}