package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    void registerUser(UserDto user);
    boolean findByEmail(String email);
    void updateResetPasswordToken(String email, String token);
    boolean validateResetPasswordToken(String token);
    void updateChangePassword(String resetPasswordToken, String password);
}
