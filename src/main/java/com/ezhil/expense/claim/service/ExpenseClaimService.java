package com.ezhil.expense.claim.service;

import com.ezhil.expense.audit.dto.ClaimAuditResponse;
import com.ezhil.expense.claim.dto.CreateExpenseClaimRequest;
import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.claim.dto.UpdateExpenseClaimRequest;

import java.util.List;
import java.util.UUID;

public interface ExpenseClaimService {

    ExpenseClaimResponse createClaim(
            CreateExpenseClaimRequest request,
            String email
    );
    List<ExpenseClaimResponse> getMyClaims(
            String email
    );
    void submitClaim(
            UUID claimId,
            String email
    );
    ExpenseClaimResponse getClaim(
            UUID claimId,
            String email
    );
    ExpenseClaimResponse updateClaim(
            UUID claimId,
            UpdateExpenseClaimRequest request,
            String email
    );

    void deleteClaim(
            UUID claimId,
            String email
    );

    void cancelClaim(
            UUID claimId,
            String email
    );
    List<ClaimAuditResponse> getClaimTimeline(
            UUID claimId,
            String email
    );
}