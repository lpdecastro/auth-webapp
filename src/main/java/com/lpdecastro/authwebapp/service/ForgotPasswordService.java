package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public void forgotPassword(String email) throws MessagingException, IOException {
        boolean userExists = userRepository.existsByEmail(email);
        if (!userExists) {
            throw new IllegalArgumentException("Email does not exist");
        }

        String token = RandomString.make(25);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String resetPasswordLink = baseUrl + "/reset-password?token=" + token;

        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setResetPasswordToken(token);
        userEntity.setTokenGeneratedDate(LocalDateTime.now());

        userRepository.save(userEntity);
        emailService.sendResetPasswordEmail(email, userEntity.getFirstName(), resetPasswordLink);
    }
}
