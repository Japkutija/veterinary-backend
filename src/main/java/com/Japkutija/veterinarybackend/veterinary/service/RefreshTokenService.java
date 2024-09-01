package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import java.util.Optional;

public interface RefreshTokenService {
    void deleteByToken(String refreshToken);
    RefreshToken findByToken(String refreshToken);
    void saveRefreshToken(RefreshToken refreshToken);
}
