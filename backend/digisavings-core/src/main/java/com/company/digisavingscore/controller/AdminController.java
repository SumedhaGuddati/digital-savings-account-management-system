package com.company.digisavingscore.controller;

import com.company.digisavingscore.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    /**
     * Promote a user to ADMIN role.
     * URL: POST /api/admin/promote/{userId}
     */
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/promote/{userId}")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable Long userId) {
        adminService.promoteUserToAdmin(userId);
        return ResponseEntity.ok("User promoted to ADMIN successfully.");
    }
}
