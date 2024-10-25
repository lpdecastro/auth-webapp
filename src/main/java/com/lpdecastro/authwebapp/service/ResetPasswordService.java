package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void resetPassword(String token, String newPassword) throws MessagingException, IOException {
        UserEntity user = validateResetPasswordToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        user.setResetPasswordTokenDate(null);

        userRepository.save(user);
        emailService.sendPasswordChangedEmail(user.getEmail(), user.getFirstName());
    }

    public UserEntity validateResetPasswordToken(String token) {
        UserEntity user = userRepository.findByResetPasswordToken(token);
        if (user == null) {
            return null;
        }

        Duration tokenAge = Duration.between(user.getResetPasswordTokenDate(), LocalDateTime.now());
        if (tokenAge.toMinutes() > 5) {
            return null;
        }

        return user;
    }
}
