package com.example.demo.phonenumber.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PinConfig {
    @Bean
    public PasswordEncoder pinEncoder() {
        return new BCryptPasswordEncoder();
    }
}
