package com.lpdecastro.authwebapp.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(Model model, HttpSession session) {
        addSessionAttributeToModel(model, session, "successMessage");
        addSessionAttributeToModel(model, session, "errorMessage");
        addSessionAttributeToModel(model, session, "emailUnverified");
        return "login";
    }

    private void addSessionAttributeToModel(Model model, HttpSession session, String key) {
        Object attribute = session.getAttribute(key);
        if (attribute != null) {
            model.addAttribute(key, attribute);
            session.removeAttribute(key);
        }
    }
}
