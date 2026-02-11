package com.company.digisavingscore.controller;

import com.company.digisavingscore.dto.request.AccountApprovalRequest;
import com.company.digisavingscore.dto.response.AdminApplicationViewResponse;
import com.company.digisavingscore.dto.response.SavingsAccountResponse;
import com.company.digisavingscore.service.IAdminAccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for administrative operations
 * related to savings account applications.
 *
 * Provides endpoints for reviewing, approving,
 * and rejecting account applications.
 */
@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminAccountController {

    /**
     * Service handling admin account application workflows.
     */
    private final IAdminAccountService adminAccountService;

    /**
     * Retrieves all savings account applications
     * that are currently in PENDING status.
     *
     * @return list of pending account applications
     */
    @GetMapping("/applications/pending")
    public ResponseEntity<List<AdminApplicationViewResponse>> getPendingApplications() {
        log.info("GET /admin/applications/pending");

        List<AdminApplicationViewResponse> applications =
                adminAccountService.getPendingApplications();

        log.info("Returning {} pending applications", applications.size());
        return ResponseEntity.ok(applications);
    }

    /**
     * Approves a savings account application
     * and creates a savings account for the customer.
     *
     * @param id      application identifier
     * @param request approval details provided by the admin
     * @return details of the created savings account
     */
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

    /**
     * Rejects a savings account application
     * with the provided rejection reason.
     *
     * @param id      application identifier
     * @param request rejection details provided by the admin
     * @return response indicating application rejection
     */
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