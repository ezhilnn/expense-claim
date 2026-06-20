package com.ezhil.expense.audit.service;

import com.ezhil.expense.audit.dto.ClaimAuditResponse;
import com.ezhil.expense.claim.entity.ExpenseClaim;
import com.ezhil.expense.common.enums.ClaimStatus;

import java.util.List;

public interface ClaimAuditService {

    void createAudit(
            ExpenseClaim claim,
            ClaimStatus oldStatus,
            ClaimStatus newStatus,
            String comments,
            String changedBy
    );

    List<ClaimAuditResponse> getClaimTimeline(
            ExpenseClaim claim
    );

}