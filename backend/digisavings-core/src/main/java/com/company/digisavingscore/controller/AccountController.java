package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.TransactionRequest;
import com.company.digisavingscore.dto.response.AccountDetailsResponse;
import com.company.digisavingscore.dto.response.TransactionHistoryResponse;
import com.company.digisavingscore.dto.response.TransactionResponse;
import com.company.digisavingscore.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts/me")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @GetMapping
    public ResponseEntity<AccountDetailsResponse> getMyAccount() {
        Long accountId = getCurrentUserAccountId();
        return ResponseEntity.ok(accountService.getAccountDetails(accountId));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.deposit(request));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@RequestBody TransactionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(accountService.withdraw(request));
    }

    @GetMapping("/transactions")
    public ResponseEntity<TransactionHistoryResponse> getMyTransactions() {
        return ResponseEntity.ok(accountService.getTransactionHistory(getCurrentUserAccountId()));
    }

    private Long getCurrentUserAccountId() {
        // TODO: Replace with auth context
        return 1L;
    }
}
