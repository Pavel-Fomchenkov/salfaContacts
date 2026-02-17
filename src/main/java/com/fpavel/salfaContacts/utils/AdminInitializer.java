package com.fpavel.salfaContacts.utils;

import com.fpavel.salfaContacts.model.User;
import com.fpavel.salfaContacts.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class AdminInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);
    @Value("${FIRST_ADMIN_LOGIN}")
    private String adminLogin;
    @Value("${FIRST_ADMIN_PASSWORD}")
    private String adminPassword;

    public AdminInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        int attempts = 0;
        while (attempts < 5) {
            try {
                if (!userRepository.existsByRole(User.Role.ADMIN)) {
                    User admin = new User();
                    admin.setLogin(adminLogin);
                    admin.setPasswordEncrypted(passwordEncoder.encode(adminPassword));
                    admin.setRole(User.Role.ADMIN);
                    userRepository.save(admin);
                    log.warn("Admin created");
                }
                break;
            } catch (Exception e) {
                attempts++;
                log.warn("Failed to create admin, attempt {} of 5", attempts, e);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
