package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    boolean isEmailExists(String email);
    UserEntity findByEmail(String email);
    boolean validateResetPasswordToken(String token);
    UserEntity updateChangePassword(String resetPasswordToken, String password);
    void updateEmailVerificationToken(String email, String token);
    boolean validateEmailVerificationToken(String token);
    void updateEmailVerifiedDate(String token);
    boolean changePassword(String username, ChangePasswordDto changePasswordDto);
}
