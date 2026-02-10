package com.company.digisavingscore.controller;

import com.company.digisavingscore.service.impl.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/promote/{userId}")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable Long userId) {

        adminService.promoteUserToAdmin(userId);

        return ResponseEntity.ok("User promoted to ADMIN successfully.");
    }
}
