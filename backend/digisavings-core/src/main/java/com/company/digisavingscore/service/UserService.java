package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.UserResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
