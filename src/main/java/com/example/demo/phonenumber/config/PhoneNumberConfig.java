package com.example.demo.phonenumber.config;

import com.example.demo.phonenumber.PhoneNumberConstants;
import com.example.demo.phonenumber.repository.PhoneNumberRepository;
import com.example.demo.phonenumber.jpamodel.PhoneNumber;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class PhoneNumberConfig {
    private final PasswordEncoder pinEncoder;
    @Bean
    CommandLineRunner commandLineRunner(PhoneNumberRepository phoneNumberRepository) {
        return args -> {
            PhoneNumber pavelMichalec = new PhoneNumber();
            pavelMichalec.setOwnerName("Pavel Michalec");
            pavelMichalec.setPrefix("+420");
            pavelMichalec.setPhoneNumber("123456789");
            pavelMichalec.setEncryptedPin(pinEncoder.encode(PhoneNumberConstants.DEFAULT_PIN));

            PhoneNumber testTestovic = new PhoneNumber();
            testTestovic.setOwnerName("Test Testovic");
            testTestovic.setPrefix("+43");
            testTestovic.setPhoneNumber("9876543210987");
            testTestovic.setEncryptedPin(pinEncoder.encode(PhoneNumberConstants.DEFAULT_PIN));

            phoneNumberRepository.saveAll(Arrays.asList(pavelMichalec, testTestovic));
        };
    }
}
