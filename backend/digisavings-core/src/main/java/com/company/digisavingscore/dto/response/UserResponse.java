package com.company.digisavingscore.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Guddati Venkata Sri Sumedha
 */
@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
