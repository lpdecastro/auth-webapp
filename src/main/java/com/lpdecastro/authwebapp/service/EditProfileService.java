package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import com.lpdecastro.authwebapp.util.SecurityUtils;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class EditProfileService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final LoginModelMapper loginModelMapper;

    public UserDto getCurrentUser() {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new IllegalStateException("No authenticated user found. Please log in.");
        }
        UserEntity userEntity = userRepository.findByEmail(email);
        return loginModelMapper.convertEntityToDto(userEntity);
    }

    @Transactional
    public void editProfile(UserDto userDto) throws MessagingException, IOException {
        String email = SecurityUtils.getCurrentUsername();
        if (email == null) {
            throw new IllegalStateException("No authenticated user found. Please log in.");
        }

        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        if (!email.equals(userDto.getEmail())) {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new IllegalArgumentException("Email is already in use.");
            }

            String token = RandomString.make(25);

            userEntity.setEmail(userDto.getEmail());
            userEntity.setEmailVerifiedDate(null);
            userEntity.setEmailVerificationToken(token);

            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String emailVerificationLink = baseUrl + "/verify-email?token=" + token;
            emailService.sendEmailChangedVerificationEmail(userEntity.getEmail(), userEntity.getFirstName(), emailVerificationLink);

            SecurityUtils.updateCurrentUsername(userDto.getEmail());
        }

        userRepository.save(userEntity);
    }
}
