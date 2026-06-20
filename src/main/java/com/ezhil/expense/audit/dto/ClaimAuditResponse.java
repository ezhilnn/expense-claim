package com.ezhil.expense.audit.dto;

import com.ezhil.expense.common.enums.ClaimStatus;

import java.time.LocalDateTime;

public record ClaimAuditResponse(

        ClaimStatus oldStatus,

        ClaimStatus newStatus,

        String comments,

        String changedBy,

        LocalDateTime changedAt
) {
}