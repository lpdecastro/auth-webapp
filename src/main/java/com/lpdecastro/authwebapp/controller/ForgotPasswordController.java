package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.service.ForgotPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(String email, Model model, RedirectAttributes redirectAttributes) {
        try {
            forgotPasswordService.forgotPassword(email);
            redirectAttributes.addFlashAttribute("successMessage", "Reset password link sent to you email successfully.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to send email. Please try again.");
        }
        return "forgot-password";
    }
}
