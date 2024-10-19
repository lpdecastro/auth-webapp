package com.lpdecastro.authwebapp.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SpringSecurityCustomConfiguration {

    Logger logger = LoggerFactory.getLogger(SpringSecurityCustomConfiguration.class);

//    @Bean
//    InMemoryUserDetailsManager userDetailsManager() {
//        UserDetails userDetails1 = User.withUsername("admin").password(passwordEncoder().encode("admin")).build();
//        UserDetails userDetails2 = User.withUsername("superuser").password(passwordEncoder().encode("superuser")).build();
//        return new InMemoryUserDetailsManager(userDetails1, userDetails2);
//    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("--- Spring Security Custom Configuration ---");
        return http.formLogin(Customizer.withDefaults())
                .authorizeHttpRequests(authorize ->
                        authorize.requestMatchers("/").authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(login ->
                        login.loginPage("/login")
                                .defaultSuccessUrl("/")
                                .failureUrl("/login?error=true"))
                .logout(logout ->
                        logout.invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout=true"))
                .build();
    }
}
