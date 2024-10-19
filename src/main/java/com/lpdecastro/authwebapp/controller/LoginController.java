package com.lpdecastro.authwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", defaultValue = "false") boolean error,
                        @RequestParam(value = "logout", defaultValue = "false") boolean logout,
                        @RequestParam(value = "userRegistered", defaultValue = "false") boolean userRegistered,
                        @RequestParam(value = "changePassword", defaultValue = "false") boolean changePassword,
                        @RequestParam(value = "resendVerification", defaultValue = "false") boolean resendVerification,
                        Model model) {
        if (error) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        } else if (logout) {
            model.addAttribute("successMessage", "You have been logged out.");
        } else if (userRegistered) {
            model.addAttribute("successMessage", "You have been registered.");
        } else if (changePassword) {
            model.addAttribute("successMessage", "You have changed your password.");
        } else if (resendVerification) {
            model.addAttribute("successMessage", "You have resent your verification code.");
        }
        return "login";
    }
}
