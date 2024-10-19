package com.lpdecastro.authwebapp.repository;

import com.lpdecastro.authwebapp.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
    UserEntity findByResetPasswordToken(String resetPasswordToken);
    UserEntity findByEmailVerificationToken(String emailVerificationToken);
}
