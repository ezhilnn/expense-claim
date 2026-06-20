package com.ezhil.expense.common.exception;

public class ClaimNotFoundException
        extends RuntimeException {

    public ClaimNotFoundException() {
        super("Claim not found");
    }
}