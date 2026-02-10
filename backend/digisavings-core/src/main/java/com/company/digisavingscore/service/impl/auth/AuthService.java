package com.company.digisavingscore.service.impl.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;
import com.company.digisavingscore.entity.User;
import com.company.digisavingscore.exception.EmailAlreadyExistsException;
import com.company.digisavingscore.exception.InvalidCredentialsException;
import com.company.digisavingscore.mapper.UserMapper;
import com.company.digisavingscore.repository.UserRepository;
import com.company.digisavingscore.security.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;

    @Override
    public AuthResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered");
        }

        User user = userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new InvalidCredentialsException("Invalid email or password")
                );

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
