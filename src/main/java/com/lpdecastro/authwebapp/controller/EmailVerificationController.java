package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.service.EmailService;
import com.lpdecastro.authwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        // validate email verification token
        boolean isTokenValid = userService.validateEmailVerificationToken(token);
        if (isTokenValid) {
            // if token is valid, then display success screen
            userService.updateEmailVerifiedDate(token);
            model.addAttribute("successMessage", "Email successfully verified!");
        } else {
            // if token is invalid, then display error screen
            model.addAttribute("errorMessage", "Email verification failed!");
        }
        return "verify-email";
    }

    @GetMapping("/resend-email-verification")
    public String resendEmailVerificationPage() {
        return "resend-email-verification";
    }

    @PostMapping("/resend-email-verification")
    public String resendEmailVerification(String email, Model model) {
        // check if email exists
        boolean isUserExists = userService.isEmailExists(email);
        if (isUserExists) {
            // if email exists, send verification link
            // create email verification token
            String token = RandomString.make(25);
            // create email verification link
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String emailVerificationLink = baseUrl + "/verify-email?token=" + token;
            // update user's email verification token
            userService.updateEmailVerificationToken(email, token);
            // send email verification link
            String emailSubject = "Resend Email Verification";
            emailService.sendEmail(email, emailSubject, emailVerificationLink);
            return "redirect:login?resendVerification=true";
        } else {
            // if email does not exists, display error
            model.addAttribute("errorMessage", "User not found!");
        }
        return "resend-email-verification";
    }
}
