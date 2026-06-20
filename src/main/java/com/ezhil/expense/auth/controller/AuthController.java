package com.ezhil.expense.auth.controller;

import com.ezhil.expense.auth.dto.LoginRequest;
import com.ezhil.expense.auth.dto.LoginResponse;
import com.ezhil.expense.auth.dto.RegisterUserRequest;
import com.ezhil.expense.auth.dto.RegisterUserResponse;
import com.ezhil.expense.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.ezhil.expense.common.response.ApiResponse;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RegisterUserResponse> register(
            @Valid @RequestBody RegisterUserRequest request
    ) {

        RegisterUserResponse response =
                authService.register(request);

        return new ApiResponse<>(
                true,
                "User registered successfully",
                response
        );
    }
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(
            @Valid
            @RequestBody
            LoginRequest request
    ) {

        LoginResponse response =
                authService.login(request);

        return new ApiResponse<>(
                true,
                "Login successful",
                response
        );
    }
}