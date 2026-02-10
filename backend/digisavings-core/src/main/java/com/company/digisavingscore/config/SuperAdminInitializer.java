package com.company.digisavingscore.config;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
public class SuperAdminInitializer {

    @Bean
    CommandLineRunner createSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if SUPER_ADMIN already exists
            if (userRepository.findByEmail("superadmin@bank.com").isEmpty()) {
                User superAdmin = new User();
                superAdmin.setName("Super Admin");
                superAdmin.setEmail("superadmin@bank.com");
                superAdmin.setPassword(passwordEncoder.encode("superpassword"));
                superAdmin.setRole(Role.SUPER_ADMIN);
                superAdmin.setCreatedAt(LocalDateTime.now());

                userRepository.save(superAdmin);
                System.out.println("SUPER_ADMIN created successfully!");
            } else {
                System.out.println("SUPER_ADMIN already exists.");
            }
        };
    }
}

