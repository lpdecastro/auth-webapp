package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("/register")
    public String showRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(UserDto userDto, Model model, RedirectAttributes redirectAttributes) {
        try {
            registrationService.register(userDto);
            redirectAttributes.addFlashAttribute("successMessage", "You have been registered.");
            return "redirect:login";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to register. Please try again.");
        }
        return "register";
    }
}
