package com.lpdecastro.authwebapp.exception;

import org.springframework.security.authentication.InternalAuthenticationServiceException;

public class EmailNotVerifiedException extends InternalAuthenticationServiceException {

    public EmailNotVerifiedException(String message) {
        super(message);
    }
}

