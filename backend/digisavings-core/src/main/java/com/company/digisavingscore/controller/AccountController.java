package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.dto.response.UserProfileDetailsResponse;
import com.company.digisavingscore.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/me")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    /**
     * User: get my account details
     * URL: GET /api/accounts/me
     */
    @GetMapping
    public ResponseEntity<AccountDetailsResponse> getMyAccount() {
        return ResponseEntity.ok(accountService.getAccountDetails());
    }

    /**
     * User: get my profile details
     * URL: GET /api/accounts/me/profile
     */
    @GetMapping("/profile")
    public ResponseEntity<UserProfileDetailsResponse> getMyProfile() {
        return ResponseEntity.ok(accountService.getProfileDetails());
    }

    /**
     * User: deposit transaction
     * URL: POST /api/accounts/me/deposit
     */
    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.deposit(request));
    }

    /**
     * User: withdraw transaction
     * URL: POST /api/accounts/me/withdraw
     */
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.withdraw(request));
    }

    /**
     * User: get all transactions
     * URL: GET /api/accounts/me/transactions
     */
    @GetMapping("/transactions")
    public ResponseEntity<TransactionHistoryResponse> getMyTransactions() {
        return ResponseEntity.ok(accountService.getTransactionHistory());
    }
}