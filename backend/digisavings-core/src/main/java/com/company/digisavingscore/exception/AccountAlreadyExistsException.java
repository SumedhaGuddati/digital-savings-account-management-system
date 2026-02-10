// File: src/main/java/com/company/digisavingscore/exception/AccountAlreadyExistsException.java
package com.company.digisavingscore.exception;

// Extending your team's existing base exception!
public class AccountAlreadyExistsException extends BusinessRuleViolationException {
    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}