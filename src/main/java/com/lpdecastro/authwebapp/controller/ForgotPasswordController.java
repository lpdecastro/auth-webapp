package com.lpdecastro.authwebapp.controller;

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

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(String email, Model model, HttpServletRequest request) {
        boolean emailExists = userService.isEmailExists(email);
        if (emailExists) {
            // create reset password token
            String token = RandomString.make(25);

            String webUrl = request.getRequestURL().toString();
            String link = webUrl.replace(request.getServletPath(), "");
            String resetPasswordLink = link + "/reset-password?token=" + token;

            // update reset password token in user table
            userService.updateResetPasswordToken(email, token);

            // send an email to user with reset password link
            String emailSubject = "Reset password link";
//            emailService.sendEmail(email, emailSubject, resetPasswordLink);

            try {
                emailService.sendResetPasswordEmail(email, "", resetPasswordLink);
            } catch (MessagingException | IOException e) {
                // Log error (optional) and display error message
                e.printStackTrace();
                model.addAttribute("errorMessage", "Failed to send email. Please try again.");
                return "forgot-password";
            }

            model.addAttribute("successMessage", "Reset password link sent to you email successfully.");
        } else {
            model.addAttribute("errorMessage", "Email does not exists");
        }
        return "forgot-password";
    }
}
