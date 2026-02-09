package com.company.digisavingscore.exception;

public class InsufficientBalanceException extends BusinessRuleViolationException{
    public InsufficientBalanceException() {
        super("Insufficient balance");
    }
}
