package com.ezhil.expense.security.jwt;

import com.ezhil.expense.user.entity.User;

public interface JwtService {

    String generateToken(User user);

    String extractUsername(String token);

    boolean isTokenValid(
            String token,
            String username
    );
}