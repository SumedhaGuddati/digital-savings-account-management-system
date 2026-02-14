package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.UserResponse;
import java.util.List;

/**
 * @author Guddati Venkata Sri Sumedha
 */
public interface IUserService {
    List<UserResponse> getAllUsers();
}

