package com.ezhil.expense.claim.dto;

import com.ezhil.expense.common.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ExpenseClaimResponse(

        UUID id,

        String title,

        String description,

        BigDecimal amount,

        ClaimStatus status,

        LocalDateTime createdAt
) {
}