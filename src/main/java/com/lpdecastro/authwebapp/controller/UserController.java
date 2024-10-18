package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/userRegistrationPage")
    public String userRegistrationPage() {
        return "userRegistration";
    }

    @PostMapping("/registerUser")
    public String registerUser(UserDto user, Model model) {
        boolean userExists = userService.findByEmail(user.getEmail());
        if (userExists) {
            model.addAttribute("errorMessage", "This email already exists");
            return "userRegistration";
        }
        userService.registerUser(user);
        return "redirect:login?userRegistered=true";
    }

    @PostMapping("/findByEmail")
    @ResponseBody
    public boolean findByEmail(String email) {
        return userService.findByEmail(email);
    }
}
