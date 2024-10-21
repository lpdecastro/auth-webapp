package com.lpdecastro.authwebapp.util;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    public static void updateCurrentUsername(String newUsername) {
        Authentication currentAuthentication = SecurityContextHolder.getContext().getAuthentication();

        if (currentAuthentication != null && currentAuthentication.isAuthenticated()) {
            Authentication newAuthentication = new UsernamePasswordAuthenticationToken(
                    newUsername,
                    currentAuthentication.getCredentials(),
                    currentAuthentication.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(newAuthentication);
        } else {
            throw new IllegalStateException("Cannot update username because no user is authenticated.");
        }
    }
}
