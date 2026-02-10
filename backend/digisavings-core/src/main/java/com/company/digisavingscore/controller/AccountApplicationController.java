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

    private final AccountApplicationService service;

    @PostMapping("/apply")
    public ResponseEntity<AccountApplicationResponse> apply(@Valid @RequestBody AccountApplicationRequest request) {
        AccountApplicationResponse response = service.applyForAccount(request);
        // Returns 201 Created for a successful submission
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/application/status/{userId}")
    public ResponseEntity<AccountApplicationResponse> getApplicationStatus(@PathVariable Long userId) {
        AccountApplicationResponse response = service.getApplicationStatus(userId);
        // Returns 200 OK
        return ResponseEntity.ok(response);
    }
}