package com.company.digisavingscore.exception;

public class AccountInactiveException extends BusinessRuleViolationException {
    public AccountInactiveException() {
        super("Account is not active");
    }
}
