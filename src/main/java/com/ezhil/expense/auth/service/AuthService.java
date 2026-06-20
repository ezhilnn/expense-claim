package com.ezhil.expense.auth.service;

import com.ezhil.expense.auth.dto.LoginRequest;
import com.ezhil.expense.auth.dto.LoginResponse;
import com.ezhil.expense.auth.dto.RegisterUserRequest;
import com.ezhil.expense.auth.dto.RegisterUserResponse;

public interface AuthService {

    RegisterUserResponse register(RegisterUserRequest request);
    LoginResponse login(LoginRequest request);
}