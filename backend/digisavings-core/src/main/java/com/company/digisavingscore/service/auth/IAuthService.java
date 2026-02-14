package com.company.digisavingscore.service.auth;

import com.company.digisavingscore.dto.request.LoginRequest;
import com.company.digisavingscore.dto.request.SignupRequest;
import com.company.digisavingscore.dto.response.auth.AuthResponse;

/**
 * @author Guddati Venkata Sri Sumedha
 */
public interface IAuthService {
    AuthResponse signup(SignupRequest signupRequest);

    AuthResponse login(LoginRequest loginRequest);
}
