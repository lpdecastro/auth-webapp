package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ResetPasswordController {

    private final UserService userService;

    @GetMapping("/resetPassword")
    public String resetPasswordWebPage(@RequestParam String token, Model model) {
        // validate the reset password token
        boolean tokenValid = userService.validateResetPasswordToken(token);
        if (tokenValid) {
            // if token is valid, then we display change password page
            model.addAttribute("resetPasswordToken", token);
            return "changePassword";
        } else {
            // if token is invalid, then display error message
            return "message";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(String resetPasswordToken, String password) {
        boolean tokenValid = userService.validateResetPasswordToken(resetPasswordToken);
        if (tokenValid) {
            // token is valid, then update password in database
            userService.updateChangePassword(resetPasswordToken, password);
        } else {
            // token is invalid, then display error message
            return "message";
        }
        return "redirect:login?changePassword=true";
    }
}
