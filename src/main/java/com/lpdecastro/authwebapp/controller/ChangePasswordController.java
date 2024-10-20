package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.service.EmailService;
import com.lpdecastro.authwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {

    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordDto", new ChangePasswordDto());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordDto changePasswordDto,
                                 Authentication authentication,
                                 Model model) {

        // Get the currently authenticated user's email (username)
        String username = authentication.getName();

        // Attempt to change the password
        boolean success = userService.changePassword(username, changePasswordDto);

        if (!success) {
            // If the current password is incorrect, return an error message
            model.addAttribute("errorMessage", "Current password is incorrect.");
            return "change-password";
        }

        // If password change was successful, add a success message
        model.addAttribute("successMessage", "Password changed successfully.");

        // Send an email notification after the password change
        UserEntity user = userService.findByEmail(username);
        String emailSubject = "Your password has been changed";
        String emailMessage = "Dear " + user.getFirstName() + ",\n\n" +
                "Your password has been successfully updated. If you did not initiate this change, please contact support immediately.\n\n" +
                "Best regards,\n" +
                "Your Support Team";

        // Send the email notification
        emailService.sendEmail(user.getEmail(), emailSubject, emailMessage);

        return "change-password";
    }

}
