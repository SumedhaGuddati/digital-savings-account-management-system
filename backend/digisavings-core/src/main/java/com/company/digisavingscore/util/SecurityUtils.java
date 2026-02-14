package com.company.digisavingscore.util;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * @author Khamitkar Sai Chandan
 */
public class SecurityUtils {

    public static String getCurrentUserEmail() {
        return Objects.requireNonNull(SecurityContextHolder.getContext()
                        .getAuthentication())
                .getName();
    }
}
