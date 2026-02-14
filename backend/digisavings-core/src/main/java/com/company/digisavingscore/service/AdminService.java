package com.company.digisavingscore.service;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.exception.ResourceNotFoundException;
import com.company.digisavingscore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserRepository userRepository;

    public void promoteUserToAdmin(Long userId) {
        log.info("Attempting to promote user with id: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("Promotion failed. User not found with id: {}", userId);
                    return new ResourceNotFoundException("User not found");
                });

        user.setRole(Role.ADMIN);
        userRepository.save(user);

        log.info("User with id {} promoted to ADMIN successfully", userId);
    }
}
