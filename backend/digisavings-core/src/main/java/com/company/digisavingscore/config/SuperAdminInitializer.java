package com.company.digisavingscore.config;

import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Configuration
public class SuperAdminInitializer {

    @Bean
    CommandLineRunner createSuperAdmin(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByEmail("superadmin@bank.com").isEmpty()) {

                User superAdmin = User.builder()
                        .name("Super Admin")
                        .email("superadmin@bank.com")
                        .password(passwordEncoder.encode("superpassword"))
                        .role(Role.SUPER_ADMIN)
                        .createdAt(LocalDateTime.now())
                        .build();

                userRepository.save(superAdmin);
                System.out.println("SUPER_ADMIN created successfully!");
            } else {
                System.out.println("SUPER_ADMIN already exists.");
            }
        };
    }
}
