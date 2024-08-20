package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

public interface UserService {
    @Transactional
    User registerUser(String username, String email, String password);
}
