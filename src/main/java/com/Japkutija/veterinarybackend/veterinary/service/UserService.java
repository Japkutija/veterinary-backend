package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.entity.User;

public interface UserService {

    User registerUser(String username, String email, String password);

    User findByUsername(String username);
}
