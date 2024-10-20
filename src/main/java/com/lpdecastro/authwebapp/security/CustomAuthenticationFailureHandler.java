package com.lpdecastro.authwebapp.security;

import com.lpdecastro.authwebapp.exception.EmailNotVerifiedException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // Set the default failure URL
    private static final String DEFAULT_FAILURE_URL = "/login?error=true";

    public CustomAuthenticationFailureHandler() {
        // Set the default failure URL in the constructor
        setDefaultFailureUrl(DEFAULT_FAILURE_URL);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        if (exception instanceof EmailNotVerifiedException) {
            getRedirectStrategy().sendRedirect(request, response, "/login?emailUnverified=true");
        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}
