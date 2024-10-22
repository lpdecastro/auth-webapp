package com.lpdecastro.authwebapp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityCustomConfiguration {

    private final CustomAuthenticationFailureHandler customFailureHandler;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomLogoutSuccessHandler customLogoutSuccessHandler;


    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    LoginUrlAuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        LoginUrlAuthenticationEntryPoint entryPoint = new LoginUrlAuthenticationEntryPoint("/login");
        entryPoint.setUseForward(true);
        return entryPoint;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/", "/edit-profile", "/change-password").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(login ->
                        login.loginPage("/login")
                                .defaultSuccessUrl("/")
                                .failureHandler(customFailureHandler)
                                .successHandler(customAuthenticationSuccessHandler)
                )
                .logout(logout ->
                        logout.invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutSuccessHandler(customLogoutSuccessHandler)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                ).exceptionHandling(exception ->
                        exception.authenticationEntryPoint(loginUrlAuthenticationEntryPoint())
                )
                .build();
    }
}
