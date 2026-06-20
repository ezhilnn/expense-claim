package com.ezhil.expense.auth.dto;

import com.ezhil.expense.common.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email,
        Role role,
        Boolean active,
        LocalDateTime createdAt
) {
}