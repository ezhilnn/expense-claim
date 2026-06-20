package com.ezhil.expense.manager.service;

import com.ezhil.expense.claim.dto.ExpenseClaimResponse;

import java.util.List;
import java.util.UUID;

public interface ManagerService {

    List<ExpenseClaimResponse> getSubmittedClaims();

    void approveClaim(UUID claimId);

    void rejectClaim(
            UUID claimId,
            String comments
    );
}