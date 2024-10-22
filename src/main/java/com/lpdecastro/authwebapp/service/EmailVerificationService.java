package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public void verifyEmail(String token) {
        UserEntity user = validateToken(token);
        if (user == null) {
            throw new IllegalArgumentException("Email verification link is invalid or expired");
        }
        user.setEmailVerifiedDate(LocalDateTime.now());
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenDate(null);
        userRepository.save(user);
    }

    public UserEntity validateToken(String token) {
        UserEntity user = userRepository.findByEmailVerificationToken(token);
        if (user == null) {
            return null;
        }

        Duration tokenAge =  Duration.between(user.getEmailVerificationTokenDate(), LocalDateTime.now());
        if (tokenAge.toMinutes() > 5) {
            return null;
        }

        return user;
    }

    public void resendEmailVerification(String email) throws MessagingException, IOException {
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Email not found");
        }

        String token = RandomString.make(25);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String emailVerificationLink = baseUrl + "/verify-email?token=" + token;

        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenDate(LocalDateTime.now());

        userRepository.save(user);
        emailService.sendResendEmailVerification(email, user.getFirstName(), emailVerificationLink);
    }
}
