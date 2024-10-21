package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.service.EmailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EmailVerificationController {
    
    private final EmailVerificationService emailVerificationService;

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        try {
            emailVerificationService.verifyEmail(token);
            model.addAttribute("successMessage", "Email successfully verified!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "verify-email";
    }

    @GetMapping("/resend-email-verification")
    public String resendEmailVerificationPage() {
        return "resend-email-verification";
    }

    @PostMapping("/resend-email-verification")
    public String resendEmailVerification(String email, Model model) {
        try {
            emailVerificationService.resendEmailVerification(email);
            return "redirect:login?resendVerification=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to send email. Please try again.");
        }
        return "resend-email-verification";
    }
}
