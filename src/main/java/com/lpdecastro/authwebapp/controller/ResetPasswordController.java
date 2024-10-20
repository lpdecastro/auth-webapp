package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.service.EmailService;
import com.lpdecastro.authwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        // validate the reset password token
        boolean tokenValid = userService.validateResetPasswordToken(token);
        if (tokenValid) {
            // if token is valid, then we display change password page
            model.addAttribute("resetPasswordToken", token);
            return "reset-password";
        } else {
            // if token is invalid, then display error message
            return "reset-password-link-error";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(String resetPasswordToken, String password) {
        // Validate the reset token
        boolean tokenValid = userService.validateResetPasswordToken(resetPasswordToken);
        if (tokenValid) {
            // Update the password in the database
            UserEntity user = userService.updateChangePassword(resetPasswordToken, password);

            // Send an email notification after successfully updating the password
            String emailSubject = "Your password has been changed";
            String emailMessage = "Dear " + user.getFirstName() + ",\n\n" +
                    "Your password has been successfully updated. If you did not initiate this change, please contact support immediately.\n\n" +
                    "Best regards,\n" +
                    "Your Support Team";
            emailService.sendEmail(user.getEmail(), emailSubject, emailMessage);
        } else {
            // Return error page if the token is invalid
            return "reset-password-link-error";
        }

        // Redirect to login page with a success message
        return "redirect:login?changePassword=true";
    }

}
