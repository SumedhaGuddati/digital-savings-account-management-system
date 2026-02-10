package com.company.digisavingscore.mapper;

import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.entity.enums.Role;
import com.company.digisavingscore.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMapper {
    public User toEntity(SignupRequest dto, String encodedPassword) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(encodedPassword)
                .role(Role.USER)   // FORCE USER
                .createdAt(LocalDateTime.now())
                .build();
    }
}
