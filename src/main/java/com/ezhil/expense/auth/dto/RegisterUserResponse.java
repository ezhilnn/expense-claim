package com.ezhil.expense.auth.dto;

public record RegisterUserResponse(
        String message,
        UserResponse user
) {
}