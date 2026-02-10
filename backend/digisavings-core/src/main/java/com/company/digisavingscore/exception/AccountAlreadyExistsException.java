package com.company.digisavingscore.exception;

public class AccountAlreadyExistsException extends BusinessRuleViolationException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}