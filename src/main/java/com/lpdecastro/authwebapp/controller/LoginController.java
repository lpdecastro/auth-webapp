package com.lpdecastro.authwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(value = {"/", "/login"})
    public String login(@RequestParam(value = "error", defaultValue = "false") boolean error,
                        @RequestParam(value = "logout", defaultValue = "false") boolean logout,
                        @RequestParam(value = "userRegistered", defaultValue = "false") boolean userRegistered,
                        @RequestParam(value = "changePassword", defaultValue = "false") boolean changePassword,
                        Model model) {
        if (error) {
            model.addAttribute("errorMessage", "Invalid username or password.");
        } else if (logout) {
            model.addAttribute("logoutMessage", "You have been logged out.");
        } else if (userRegistered) {
            model.addAttribute("userRegisteredMessage", "You have been registered.");
        } else if (changePassword) {
            model.addAttribute("userRegisteredMessage", "You have changed your password.");
        }
        return "login";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }
}
