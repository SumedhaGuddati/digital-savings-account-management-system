package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.service.IAdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/accounts")
@RequiredArgsConstructor
public class AdminAccountController {

    private final IAdminAccountService adminAccountService;

    @GetMapping("/applications")
    public ResponseEntity<List<AdminApplicationViewResponse>> getPendingApplications() {
        return ResponseEntity.ok(adminAccountService.getPendingApplications());
    }

    @PutMapping("/applications/{id}/approve")
    public ResponseEntity<SavingsAccountResponse> approveApplication(
            @PathVariable Long id,
            @RequestBody AccountApprovalRequest request,
            @AuthenticationPrincipal UserDetails adminDetails) {

        return ResponseEntity.ok(adminAccountService.approveApplication(id, request, adminDetails.getUsername()));
    }

    @PutMapping("/applications/{id}/reject")
    public ResponseEntity<SavingsAccountResponse> rejectApplication(
            @PathVariable Long id,
            @RequestBody AccountApprovalRequest request,
            @AuthenticationPrincipal UserDetails adminDetails) {

        return ResponseEntity.ok(adminAccountService.rejectApplication(id, request, adminDetails.getUsername()));
    }
}