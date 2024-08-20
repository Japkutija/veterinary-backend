package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.UserAlreadyExistsException;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.UserService;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User registerUser(String username, String email, String password) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyExistsException("User with such email already exists");
        }

        if (userRepository.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User with such username already exists");
        }

        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setUuid(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        log.info("User registration successful for username: `{}`", username);
        try {
            return userRepository.save(user);
        } catch (Exception e) {
            throw new BadRequestException("User registration failed", e);
        }
    }
}
