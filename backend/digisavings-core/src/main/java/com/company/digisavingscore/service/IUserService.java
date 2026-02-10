package com.company.digisavingscore.service;

import com.company.digisavingscore.dto.response.UserResponse;
import java.util.List;

public interface IUserService {
    List<UserResponse> getAllUsers();
}

