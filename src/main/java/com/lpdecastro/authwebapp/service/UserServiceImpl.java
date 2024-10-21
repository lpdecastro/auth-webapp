package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException(username + " not found");
        }
        return User.withUsername(userEntity.getEmail()).password(userEntity.getPassword()).build();
    }


    @Override
    public void updateEmailVerificationToken(String email, String token) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setEmailVerificationToken(token);
        userEntity.setEmailVerificationTokenDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    @Override
    public boolean validateEmailVerificationToken(String token) {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        if (userEntity == null) {
            return false;
        }
        LocalDateTime emailVerificationTokenDate = userEntity.getEmailVerificationTokenDate();
        Duration duration = Duration.between(emailVerificationTokenDate, LocalDateTime.now());
        return duration.toMinutes() <= 5;
    }

    @Override
    public void updateEmailVerifiedDate(String token) {
        UserEntity userEntity = userRepository.findByEmailVerificationToken(token);
        userEntity.setEmailVerifiedDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }
}
