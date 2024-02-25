package fr.ensicaen.pi.gpss.backend.tools.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderBean {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderBean() {
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
