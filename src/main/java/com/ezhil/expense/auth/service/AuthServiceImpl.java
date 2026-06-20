package com.ezhil.expense.auth.service;

import com.ezhil.expense.auth.dto.*;
import com.ezhil.expense.common.enums.Role;
import com.ezhil.expense.common.exception.EmailAlreadyExistsException;
import com.ezhil.expense.common.exception.InvalidCredentialsException;
import com.ezhil.expense.security.jwt.JwtService;
import com.ezhil.expense.user.entity.User;
import com.ezhil.expense.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import  com.ezhil.expense.security.SecurityConfig.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public RegisterUserResponse register(RegisterUserRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = User.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.EMPLOYEE)
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        UserResponse userResponse = new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole(),
                savedUser.getActive(),
                savedUser.getCreatedAt()
        );

        return new RegisterUserResponse(
                "User registered successfully",
                userResponse
        );
    }
    @Override
    public LoginResponse login(
            LoginRequest request
    ) {

        User user = userRepository
                .findByEmail(request.email())
                .orElseThrow(
                        InvalidCredentialsException::new
                );

        boolean matches =
                passwordEncoder.matches(
                        request.password(),
                        user.getPassword()
                );

        if (!matches) {
            throw new InvalidCredentialsException();
        }

        String token =
                jwtService.generateToken(user);

        return new LoginResponse(token);
    }
}