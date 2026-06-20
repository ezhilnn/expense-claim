package com.ezhil.expense.finance.service;

import com.ezhil.expense.claim.dto.ExpenseClaimResponse;

import java.util.List;
import java.util.UUID;

public interface FinanceService {

    List<ExpenseClaimResponse> getApprovedClaims();

    void approveClaim(UUID claimId);

    void rejectClaim(
            UUID claimId,
            String comments
    );

    void markAsPaid(UUID claimId);
}