package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import java.time.LocalDate;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {

    User registerUser(String username, String email, String password, String firstName, String lastName, LocalDate dateOfBirth, String emso,
            String phoneNumber, String address);

    User findByUsername(String username);
}
