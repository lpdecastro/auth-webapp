package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

    @GetMapping("/reset-password")
    public String showResetPasswordPage(@RequestParam String token, Model model) {
        UserEntity user = resetPasswordService.validateResetPasswordToken(token);
        if (user == null) {
            return "reset-password-link-error";
        }
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(String token, String password, RedirectAttributes redirectAttributes) {
        try {
            resetPasswordService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("successMessage", "You have successfully reset your password.");
            return "redirect:login";
        } catch (Exception e) {
            return "reset-password-link-error";
        }
    }
}
