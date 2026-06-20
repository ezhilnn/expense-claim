package com.ezhil.expense.finance.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectPaymentRequest(

        @NotBlank(message = "Comments are required")
        String comments

) {
}