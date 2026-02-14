package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.UserResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.service.IUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        log.info("Fetching all users from database");

        List<UserResponse> users = userRepository.findAll().stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .createdAt(user.getCreatedAt())
                        .build()
                )
                .collect(Collectors.toList());

        log.info("Retrieved {} users", users.size());
        return users;
    }
}
