package com.ezhil.expense.manager.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectClaimRequest(

        @NotBlank(message = "Comments are required")
        String comments

) {
}