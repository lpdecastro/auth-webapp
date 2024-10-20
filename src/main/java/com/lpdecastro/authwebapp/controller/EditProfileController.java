package com.lpdecastro.authwebapp.controller;

import com.lpdecastro.authwebapp.dto.UserDto;
import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.service.EmailService;
import com.lpdecastro.authwebapp.service.UserService;
import com.lpdecastro.authwebapp.util.LoginModelMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequiredArgsConstructor
public class EditProfileController {

    private final UserService userService;
    private final LoginModelMapper loginModelMapper;
    private final EmailService emailService;

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
        String currentEmail = auth.getName();

        // Fetch the current user
        UserEntity userEntity = userService.findByEmail(currentEmail);

        // Update user entity with new data
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        // Check if the email has changed and is unique
        if (!userEntity.getEmail().equals(userDto.getEmail())) {
            if (userService.findByEmail(userDto.getEmail()) != null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Email is already in use");
                return "redirect:/edit-profile";
            }

            // Update email and reset emailVerifiedDate to null
            userEntity.setEmail(userDto.getEmail());
            userEntity.setEmailVerifiedDate(null);

            // Generate a new email verification token
            String token = RandomString.make(25);
            userEntity.setEmailVerificationToken(token);

            // Save the updated user entity
            userService.updateUser(userEntity);

            // Send email verification link
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String emailVerificationLink = baseUrl + "/verify-email?token=" + token;
            emailService.sendEmail(userDto.getEmail(), "Verify Your Email", emailVerificationLink);

            // Update the Authentication object with the new email (username)
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    userEntity.getEmail(),
                    auth.getCredentials(),
                    auth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            // Inform the user to check their new email for verification
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated. Please verify your new email address.");
        } else {
            // Save the updated user entity without changing the email
            userService.updateUser(userEntity);
            redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully.");
        }

        return "redirect:/edit-profile";
    }


}
