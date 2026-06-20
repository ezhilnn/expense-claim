package com.ezhil.expense.claim.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateExpenseClaimRequest(

        @NotBlank
        String title,

        String description,

        @DecimalMin("0.01")
        BigDecimal amount
) {
}