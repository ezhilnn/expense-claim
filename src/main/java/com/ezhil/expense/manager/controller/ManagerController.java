package com.ezhil.expense.manager.controller;

import com.ezhil.expense.claim.dto.ExpenseClaimResponse;
import com.ezhil.expense.common.response.ApiResponse;
import com.ezhil.expense.manager.dto.RejectClaimRequest;
import com.ezhil.expense.manager.service.ManagerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.UUID;

@PreAuthorize("hasRole('MANAGER')")
@RestController
@RequestMapping("/api/v1/manager/claims")
@RequiredArgsConstructor
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping
    public ApiResponse<List<ExpenseClaimResponse>>
    getSubmittedClaims() {

        return new ApiResponse<>(
                true,
                "Submitted claims fetched successfully",
                managerService.getSubmittedClaims()
        );
    }

    @PostMapping("/{claimId}/approve")
    public ApiResponse<Void>
    approveClaim(
            @PathVariable UUID claimId
    ) {

        managerService.approveClaim(
                claimId
        );

        return new ApiResponse<>(
                true,
                "Claim approved successfully",
                null
        );
    }

    @PostMapping("/{claimId}/reject")
    public ApiResponse<Void>
    rejectClaim(
            @PathVariable UUID claimId,
            @Valid
            @RequestBody
            RejectClaimRequest request
    ) {

        managerService.rejectClaim(
                claimId,
                request.comments()
        );

        return new ApiResponse<>(
                true,
                "Claim rejected successfully",
                null
        );
    }
}