package com.lpdecastro.authwebapp.service;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean isEmailExists(String email);
    void updateEmailVerificationToken(String email, String token);
    boolean validateEmailVerificationToken(String token);
    void updateEmailVerifiedDate(String token);
}
