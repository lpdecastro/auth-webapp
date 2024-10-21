package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import com.lpdecastro.authwebapp.util.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ChangePasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void changePassword(ChangePasswordDto changePasswordDto) throws MessagingException, IOException {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new IllegalStateException("No authenticated user found. Please log in.");
        }
        UserEntity userEntity = userRepository.findByEmail(email);

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), userEntity.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        userEntity.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        userRepository.save(userEntity);
        emailService.sendPasswordChangedEmail(userEntity.getEmail(), userEntity.getFirstName());
    }
}
