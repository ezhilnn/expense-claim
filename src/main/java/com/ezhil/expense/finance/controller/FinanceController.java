package com.ezhil.expense.finance.controller;

import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.common.response.ApiResponse;
import com.ezhil.expense.finance.dto.RejectPaymentRequest;
import com.ezhil.expense.finance.service.FinanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/finance/claims")
@RequiredArgsConstructor
@PreAuthorize("hasRole('FINANCE')")
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping
    public ApiResponse<List<ExpenseClaimResponse>>
    getApprovedClaims() {

        return new ApiResponse<>(
                true,
                "Approved claims fetched successfully",
                financeService.getApprovedClaims()
        );
    }

    @PostMapping("/{claimId}/approve")
    public ApiResponse<Void>
    approveClaim(
            @PathVariable UUID claimId
    ) {

        financeService.approveClaim(claimId);

        return new ApiResponse<>(
                true,
                "Finance approved successfully",
                null
        );
    }

    @PostMapping("/{claimId}/reject")
    public ApiResponse<Void>
    rejectClaim(
            @PathVariable UUID claimId,
            @Valid
            @RequestBody RejectPaymentRequest request
    ) {

        financeService.rejectClaim(
                claimId,
                request.comments()
        );

        return new ApiResponse<>(
                true,
                "Finance rejected successfully",
                null
        );
    }

    @PostMapping("/{claimId}/pay")
    public ApiResponse<Void>
    markAsPaid(
            @PathVariable UUID claimId
    ) {

        financeService.markAsPaid(claimId);

        return new ApiResponse<>(
                true,
                "Claim marked as paid",
                null
        );
    }
}