package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.service.EditProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EditProfileController {

    private final EditProfileService editProfileService;

    @GetMapping("/edit-profile")
    public String showEditProfilePage(Model model) {
        UserDto userDto = editProfileService.getCurrentUser();
        model.addAttribute("user", userDto);
//        return "edit-profile-v2";
        return "edit-profile-v3-using6";
    }

    @PostMapping("/edit-profile")
    public String editProfile(UserDto user, RedirectAttributes redirectAttributes) {
        try {
            editProfileService.editProfile(user);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update profile. Please try again.");
        }
        return "redirect:/edit-profile";
    }
}
