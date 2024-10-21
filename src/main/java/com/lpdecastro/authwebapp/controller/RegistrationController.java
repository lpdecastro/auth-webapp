package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.service.EmailService;
import com.lpdecastro.authwebapp.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDto user, Model model, HttpServletRequest request) {
        boolean userExists = userService.isEmailExists(user.getEmail());
        if (userExists) {
            model.addAttribute("errorMessage", "This email already exists");
            return "register";
        }
        userService.registerUser(user);

        // create email verification token
        String token = RandomString.make(25);
        // create email verification link
        String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String emailVerificationLink = baseUrl + "/verify-email?token=" + token;
        // update user's email verification token
        userService.updateEmailVerificationToken(user.getEmail(), token);
        // send email verification link
        String emailSubject = "Registration Confirmation";
        long startTime = System.nanoTime();
//        emailService.sendEmail(user.getEmail(), emailSubject, emailVerificationLink);
        // Send registration confirmation email
        try {
            emailService.sendRegistrationConfirmationEmail(user.getEmail(), user.getFirstName(), emailVerificationLink);
        } catch (MessagingException | IOException e) {
            // Log error (optional) and display error message
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to send confirmation email. Please try again.");
            return "register";
        }
        long endTime = System.nanoTime();
        long timeElapsed = (endTime - startTime) / 1_000_000;
        System.out.println("Execution time for sendEmail: " + timeElapsed + " ms");

        return "redirect:login?userRegistered=true";
    }
}
