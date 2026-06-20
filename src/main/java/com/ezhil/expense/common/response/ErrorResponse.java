package com.ezhil.expense.common.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        boolean success,
        String message,
        LocalDateTime timestamp
) {
}