package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.exception.GeneralRunTimeException;
import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.repository.RefreshTokenRepository;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken findByToken(String refreshToken) {
        return refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> {
            log.error("Refresh token not found: `{}`", refreshToken);
            return new RuntimeException("Refresh token not found");
        });
    }

    @Override
    public void saveRefreshToken(RefreshToken refreshToken) {
        refreshTokenRepository.save(refreshToken);

    }

    @Override
    public void deleteByToken(String refreshToken) {
        try {
            refreshTokenRepository.deleteByToken(refreshToken);
        } catch (Exception e) {
            log.error("Error deleting refresh token: {}", refreshToken);
            throw new GeneralRunTimeException("Error deleting refresh token", e);
        }
    }

}
