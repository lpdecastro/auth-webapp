package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDto userDto, Model model) {
        try {
            registrationService.register(userDto);
            return "redirect:login?userRegistered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to register. Please try again.");
        }
        return "register";
    }
}
