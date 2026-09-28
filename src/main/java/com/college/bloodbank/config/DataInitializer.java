package com.college.bloodbank.config;

import com.college.bloodbank.entity.AppUser;
import com.college.bloodbank.entity.Role;
import com.college.bloodbank.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seedUsers(AppUserRepository repository) {
        return args -> {
            if (repository.findByUsername("admin").isEmpty()) repository.save(new AppUser("admin", "admin123", Role.ADMIN));
            if (repository.findByUsername("user").isEmpty()) repository.save(new AppUser("user", "user123", Role.USER));
        };
    }
}
