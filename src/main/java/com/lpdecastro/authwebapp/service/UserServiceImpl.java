package com.lpdecastro.authwebapp.service;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.RoleEntity;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.RoleRepository;
import com.lpdecastro.authwebapp.repository.UserRepository;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final LoginModelMapper loginModelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public void registerUser(UserDto userDto) {
        RoleEntity roleEntity = roleRepository.findByName("ROLE_USER");
        UserEntity userEntity = loginModelMapper.convertDtoToEntity(userDto);
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setRoles(new ArrayList<>(List.of(roleEntity)));
        userRepository.save(userEntity);
    }

    @Override
    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Override
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
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
    public void updateResetPasswordToken(String email, String token) {
        UserEntity userEntity = userRepository.findByEmail(email);
        userEntity.setResetPasswordToken(token);
        userEntity.setTokenGeneratedDate(new Timestamp(System.currentTimeMillis()));
        userRepository.save(userEntity);
    }

    @Override
    public boolean validateResetPasswordToken(String token) {
        UserEntity userEntity = userRepository.findByResetPasswordToken(token);
        if (userEntity == null) {
            return false;
        }
        long inMinutes = validateResetPasswordExpiryTime(userEntity);
        // invalidate reset password link after 5min
        return inMinutes <= 5;
    }

    @Override
    public void updateChangePassword(String resetPasswordToken, String password) {
        UserEntity userEntity = userRepository.findByResetPasswordToken(resetPasswordToken);
        userEntity.setPassword(passwordEncoder.encode(password));
        userRepository.save(userEntity);
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

    @Override
    public boolean changePassword(String username, ChangePasswordDto changePasswordDto) {
        // Find the user by email (or username)
        UserEntity userEntity = userRepository.findByEmail(username);

        // Check if current password matches
        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), userEntity.getPassword())) {
            return false; // Current password is incorrect
        }

        // Set the new password and encode it
        userEntity.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));

        // Save the user with the new password
        userRepository.save(userEntity);
        return true; // Password changed successfully
    }

    private static long validateResetPasswordExpiryTime(UserEntity userEntity) {
        Timestamp tokenGeneratedTimestamp = userEntity.getTokenGeneratedDate();
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        long differenceInMs = currentTimestamp.getTime() - tokenGeneratedTimestamp.getTime();
        return differenceInMs / 60000;
    }
}
