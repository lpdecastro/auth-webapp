package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.ChangePasswordDto;
import com.lpdecastro.authwebapp.service.ChangePasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ChangePasswordController {

    private final ChangePasswordService changePasswordService;

    @GetMapping("/change-password")
    public String showChangePasswordPage() {
        return "change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(ChangePasswordDto changePasswordDto, Model model) {
        try {
            changePasswordService.changePassword(changePasswordDto);
            model.addAttribute("successMessage", "Password changed successfully.");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to change password. Please try again.");
        }
        return "change-password";
    }

}
