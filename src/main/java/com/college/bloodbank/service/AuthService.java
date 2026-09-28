package com.college.bloodbank.service;

import com.college.bloodbank.entity.AppUser;
import com.college.bloodbank.entity.Role;
import com.college.bloodbank.exception.BusinessException;
import com.college.bloodbank.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AppUserRepository repository;
    public AuthService(AppUserRepository repository) { this.repository = repository; }

    public AppUser login(String username, String password, Role requestedRole) {
        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("Invalid username or password."));
        if (!user.getPassword().equals(password) || user.getRole() != requestedRole) {
            throw new BusinessException("Invalid username, password, or role.");
        }
        return user;
    }
}
