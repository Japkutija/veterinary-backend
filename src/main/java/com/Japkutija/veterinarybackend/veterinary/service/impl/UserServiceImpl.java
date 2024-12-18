package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.exception.UserAlreadyExistsException;
import com.Japkutija.veterinarybackend.veterinary.exception.ValidationException;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ValidationErrorResponse;
import com.Japkutija.veterinarybackend.veterinary.model.dto.response.ValidationErrorResponse.FieldError;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.UserService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;

    @Override
    @Transactional
    public User registerUser(
            String username, String email, String password, String firstName, String lastName, LocalDate dateOfBirth, String emso,
            String phoneNumber, String address) {

        var fieldErrors = new ArrayList<FieldError>();

        // Check username and email in one query
        var existingUsers = userRepository.findByUsernameOrEmailIgnoreCase(username, email);
        for (var user : existingUsers) {
            if (user.getUsername().equals(username)) {
                fieldErrors.add(new FieldError("username", "Username already exists."));
            }
            if (user.getEmail().equals(email)) {
                fieldErrors.add(new FieldError("email", "Email already exists."));
            }
        }

        // Check EMSO and phone number in one query
        var existingOwners = ownerRepository.findByEMSOOrPhoneNumber(emso, phoneNumber);
        for (var owner : existingOwners) {
            if (owner.getEMSO().equals(emso)) {
                fieldErrors.add(new FieldError("emso", "EMSO already exists."));
            }
            if (owner.getPhoneNumber().equals(phoneNumber)) {
                fieldErrors.add(new FieldError("phoneNumber", "Phone number already exists."));
            }
        }

        // Throw exception if any field errors
        if (!fieldErrors.isEmpty()) {
            throw new ValidationException(fieldErrors);
        }

        var user = User.builder()
                .username(username)
                .email(email)
                .uuid(UUID.randomUUID())
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .build();

        User result;
        try {
            result = userRepository.save(user);
            log.info("User registration successful for username: `{}`", username);

            var owner = Owner.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .dateOfBirth(dateOfBirth)
                    .EMSO(emso)
                    .uuid(UUID.randomUUID())
                    .user(user)
                    .email(email)
                    .phoneNumber(phoneNumber)
                    .address(address)
                    .build();

            ownerRepository.save(owner);
            log.info("Owner created successfully for user: {}", user.getUuid());
        } catch (Exception ex) {
            throw new BadRequestException("User or Owner registration failed", ex);
        }
        return result;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User not found with username: `{}`", username);
                    return new EntityNotFoundException(User.class, username);
                });
    }
}
