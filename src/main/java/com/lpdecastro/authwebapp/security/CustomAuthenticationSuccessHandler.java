package com.lpdecastro.authwebapp.security;

import com.lpdecastro.authwebapp.entity.UserEntity;
import com.lpdecastro.authwebapp.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String username = authentication.getName();
        UserEntity userEntity = userRepository.findByEmail(username);

        // Check if email is verified after successful login
        if (userEntity.getEmailVerifiedDate() == null) {
            request.getSession().setAttribute("emailUnverified", "true");
            response.sendRedirect("/login");
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }
}
