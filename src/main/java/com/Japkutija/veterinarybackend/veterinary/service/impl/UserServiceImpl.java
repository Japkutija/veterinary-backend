package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.BadRequestException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.EntitySavingException;
import com.Japkutija.veterinarybackend.veterinary.exception.UserAlreadyExistsException;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.UserService;
import java.time.LocalDate;
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
    public User registerUser(String username, String email, String password, String firstName, String lastName, LocalDate dateOfBirth, String emso,
        String phoneNumber, String address) {

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new UserAlreadyExistsException("Username already exists.");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new UserAlreadyExistsException("Owner with such email already exists.");
        }

        if (ownerRepository.existsByEMSO(emso)) {
            throw new UserAlreadyExistsException("Owner with such EMSO already exists.", "emso");
        }

        if(ownerRepository.existsByPhoneNumber(phoneNumber)) {
            throw new UserAlreadyExistsException("Owner with such phone number already exists.", "phoneNumber");
        }

        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setUuid(UUID.randomUUID());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.USER);

        User result;
        try {
            result = userRepository.save(user);
            log.info("User registration successful for username: `{}`", username);
        } catch (Exception e) {
            throw new BadRequestException("User registration failed", e);
        }
        // Now we need to create a new Owner
        var owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setDateOfBirth(dateOfBirth);
        owner.setEMSO(emso);
        owner.setUuid(UUID.randomUUID());
        owner.setUser(user);
        owner.setEmail(email);
        owner.setPhoneNumber(phoneNumber);
        owner.setAddress(address);

        try {
            ownerRepository.save(owner);
            log.info("Owner created successfully for user: {}", user.getUuid());
        } catch (Exception ex) {
            throw new EntitySavingException(Owner.class, ex);
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
