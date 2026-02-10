package com.company.digisavingscore.controller;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    // Only SUPER_ADMIN can hit this API
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/promote/{userId}")
    public ResponseEntity<String> promoteUserToAdmin(@PathVariable Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setRole(Role.ADMIN); // promote to ADMIN
        userRepository.save(user);

        return ResponseEntity.ok("User with ID " + userId + " promoted to ADMIN successfully.");
    }
}