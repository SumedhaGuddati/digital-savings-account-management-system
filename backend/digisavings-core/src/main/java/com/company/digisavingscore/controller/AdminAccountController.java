package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.AdminDashboardStatsResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.service.IAdminAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Bharadi Harshitha
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAccountController {


    private final IAdminAccountService adminAccountService;

    @GetMapping("/applications/approved")
    public ResponseEntity<List<AdminApplicationViewResponse>> getApprovedApplications() {
        log.info("GET /admin/applications/approved");

        List<AdminApplicationViewResponse> applications =
                adminAccountService.getApprovedApplications();

        log.info("Returning {} approved applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    @GetMapping("/applications/pending")
    public ResponseEntity<List<AdminApplicationViewResponse>> getPendingApplications() {
        log.info("GET /admin/applications/pending");

        List<AdminApplicationViewResponse> applications =
                adminAccountService.getPendingApplications();

        log.info("Returning {} pending applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/applications/{id}/approve")
    public ResponseEntity<SavingsAccountResponse> approveApplication(
            @PathVariable Long id,
            @RequestBody AccountApprovalRequest request) {

        log.info("POST /admin/applications/{}/approve", id);

        String adminUsername = "admin_user";

        SavingsAccountResponse response =
                adminAccountService.approveApplication(id, request, adminUsername);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/applications/{id}/reject")
    public ResponseEntity<SavingsAccountResponse> rejectApplication(
            @PathVariable Long id,
            @RequestBody AccountApprovalRequest request) {

        log.info("POST /admin/applications/{}/reject", id);

        String adminUsername = "admin_user";

        SavingsAccountResponse response =
                adminAccountService.rejectApplication(id, request, adminUsername);

        return ResponseEntity.ok(response);
    }
}