package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
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

    @GetMapping("/change-password")
    public String showChangePasswordForm(Model model) {
        model.addAttribute("changePasswordDto", new ChangePasswordDto());
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordDto changePasswordDto,
            Authentication authentication,
            Model model) {

        String username = authentication.getName();
        boolean success = userService.changePassword(username, changePasswordDto);

        if (!success) {
            model.addAttribute("errorMessage", "Current password is incorrect.");
            return "change-password";
        }

        model.addAttribute("successMessage", "Password changed successfully.");
        return "change-password";
    }
}
