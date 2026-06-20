package com.ezhil.expense.common.exception;

public class InvalidClaimStateException
        extends RuntimeException {

    public InvalidClaimStateException(
            String message
    ) {
        super(message);
    }
}