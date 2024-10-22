package com.lpdecastro.authwebapp.security;

import com.lpdecastro.authwebapp.repository.EmailNotVerifiedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        if (exception.getCause() instanceof EmailNotVerifiedException) {
            request.getSession().setAttribute("emailUnverified", "true");
        } else {
            request.getSession().setAttribute("errorMessage", "Invalid username or password. Please try again.");
        }
        getRedirectStrategy().sendRedirect(request, response, "/login");
    }
}
