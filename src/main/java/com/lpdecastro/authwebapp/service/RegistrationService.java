package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.Role;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.RoleRepository;
import com.lpdecastro.authwebapp.repository.UserRepository;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final LoginModelMapper loginModelMapper;

    public void register(UserDto userDto) throws MessagingException, IOException {
        boolean userExists = userRepository.existsByEmail(userDto.getEmail());
        if (userExists) {
            throw new IllegalArgumentException("Email already in use");
        }
        UserEntity userEntity = loginModelMapper.convertDtoToEntity(userDto);

        String token = RandomString.make(25);
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String emailVerificationLink = baseUrl + "/verify-email?token=" + token;

        Role role = roleRepository.findByName("ROLE_USER");

        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setEmailVerificationToken(token);
        userEntity.setEmailVerificationTokenDate(LocalDateTime.now());
        userEntity.setRoles(new HashSet<>(Set.of(role)));

        userRepository.save(userEntity);
        emailService.sendRegistrationConfirmationEmail(userDto.getEmail(), userDto.getFirstName(), emailVerificationLink);
    }
}
