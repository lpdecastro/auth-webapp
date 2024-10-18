package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.service.EmailSenderService;
import com.lpdecastro.authwebapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;
    private final EmailSenderService emailSenderService;

    @GetMapping("/viewForgotPassword")
    public String viewForgotPasswordPage() {
        return "forgotpassword";
    }

    @PostMapping("/sendEmail")
    public String sendEmail(String email, Model model, HttpServletRequest request) {
        boolean emailExists = userService.findByEmail(email);
        if (emailExists) {
            // create reset password token
            String token = RandomString.make(25);

            String webUrl = request.getRequestURL().toString();
            String link = webUrl.replace(request.getServletPath(), "");
            String resetPasswordLink = link + "/resetPassword?token=" + token;

            // update reset password token in user table
            userService.updateResetPasswordToken(email, token);

            // send an email to user with reset password link
            String emailSubject = "Reset password link";
            emailSenderService.sendEmail(email, emailSubject, resetPasswordLink);
            model.addAttribute("successMessage", "Reset password link sent to you email successfully.");
        } else {
            model.addAttribute("errorMessage", "Email does not exists");
        }
        return "forgotpassword";
    }
}
