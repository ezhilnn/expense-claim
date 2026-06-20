package com.ezhil.expense.claim.controller;

import com.ezhil.expense.audit.dto.ClaimAuditResponse;
import com.ezhil.expense.claim.dto.CreateExpenseClaimRequest;
import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.claim.dto.UpdateExpenseClaimRequest;
import com.ezhil.expense.claim.service.ExpenseClaimService;
import com.ezhil.expense.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/claims")
@RequiredArgsConstructor
public class ExpenseClaimController {

    private final ExpenseClaimService expenseClaimService;

    @PostMapping
    public ApiResponse<ExpenseClaimResponse> createClaim(
            @Valid
            @RequestBody
            CreateExpenseClaimRequest request,
            Authentication authentication
    ) {

        ExpenseClaimResponse response =
                expenseClaimService.createClaim(
                        request,
                        authentication.getName()
                );

        return new ApiResponse<>(
                true,
                "Expense claim created successfully",
                response
        );
    }
    @GetMapping("/my")
    public ApiResponse<List<ExpenseClaimResponse>>
    getMyClaims(
            Authentication authentication
    ) {

        List<ExpenseClaimResponse> claims =
                expenseClaimService.getMyClaims(
                        authentication.getName()
                );

        return new ApiResponse<>(
                true,
                "Claims fetched successfully",
                claims
        );
    }
    @PostMapping("/{claimId}/submit")
    public ApiResponse<Void> submitClaim(
            @PathVariable UUID claimId,
            Authentication authentication
    ) {

        expenseClaimService.submitClaim(
                claimId,
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Claim submitted successfully",
                null
        );
    }
    @GetMapping("/{claimId}")
    public ApiResponse<ExpenseClaimResponse>
    getClaim(
            @PathVariable UUID claimId,
            Authentication authentication
    ) {

        ExpenseClaimResponse response =
                expenseClaimService.getClaim(
                        claimId,
                        authentication.getName()
                );

        return new ApiResponse<>(
                true,
                "Claim fetched successfully",
                response
        );
    }
    @PutMapping("/{claimId}")
    public ApiResponse<ExpenseClaimResponse>
    updateClaim(
            @PathVariable UUID claimId,
            @Valid
            @RequestBody
            UpdateExpenseClaimRequest request,
            Authentication authentication
    ) {

        ExpenseClaimResponse response =
                expenseClaimService.updateClaim(
                        claimId,
                        request,
                        authentication.getName()
                );

        return new ApiResponse<>(
                true,
                "Claim updated successfully",
                response
        );
    }

    @DeleteMapping("/{claimId}")
    public ApiResponse<Void>
    deleteClaim(
            @PathVariable UUID claimId,
            Authentication authentication
    ) {

        expenseClaimService.deleteClaim(
                claimId,
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Claim deleted successfully",
                null
        );
    }
    @PostMapping("/{claimId}/cancel")
    public ApiResponse<Void>
    cancelClaim(
            @PathVariable UUID claimId,
            Authentication authentication
    ) {

        expenseClaimService.cancelClaim(
                claimId,
                authentication.getName()
        );

        return new ApiResponse<>(
                true,
                "Claim cancelled successfully",
                null
        );
    }
    @GetMapping("/{claimId}/timeline")
    public ApiResponse<List<ClaimAuditResponse>>
    getClaimTimeline(
            @PathVariable UUID claimId,
            Authentication authentication
    ) {

        List<ClaimAuditResponse> timeline =
                expenseClaimService
                        .getClaimTimeline(
                                claimId,
                                authentication.getName()
                        );

        return new ApiResponse<>(
                true,
                "Claim timeline fetched successfully",
                timeline
        );
    }
    @PostMapping("/{claimId}/receipt")
    public ApiResponse<ExpenseClaimResponse>
    uploadReceipt(
            @PathVariable UUID claimId,
            @RequestParam("file") MultipartFile file,
            Authentication authentication
    ) {

        ExpenseClaimResponse response =
                expenseClaimService.uploadReceipt(
                        claimId,
                        file,
                        authentication.getName()
                );

        return new ApiResponse<>(
                true,
                "Receipt uploaded successfully",
                response
        );
    }
}