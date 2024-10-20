package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.service.UserService;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class EditProfileController {

    private final UserService userService;
    private final LoginModelMapper loginModelMapper;

    @GetMapping("/edit-profile")
    public String editProfilePage(Model model) {
        // Get the currently authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName(); // Get the email/username of the currently logged-in user

        // Fetch the user details by email
        UserEntity userEntity = userService.findByEmail(email);

        // Create a UserDto object and populate it with the current user data
        UserDto userDto = loginModelMapper.convertEntityToDto(userEntity);

        model.addAttribute("user", userDto);
        return "edit-profile";
    }

    @PostMapping("/edit-profile")
    public String editProfile(Model model, UserDto userDto, RedirectAttributes redirectAttributes) {
        // Get the currently authenticated user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        // Fetch the current user
        UserEntity userEntity = userService.findByEmail(email);

        // Update user entity with new data
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        // Check if the email has changed and is unique
        if (!userEntity.getEmail().equals(userDto.getEmail())) {
            if (userService.findByEmail(userDto.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email is already in use");
                return "redirect:/edit-profile";
            }
            userEntity.setEmail(userDto.getEmail());
        }

        // Save the updated user entity
        userService.updateUser(userEntity);

        // Add success message and redirect
        redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully");
        return "redirect:/edit-profile";
    }
}
