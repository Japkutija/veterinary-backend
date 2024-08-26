package com.Japkutija.veterinarybackend.veterinary.service.impl;

import com.Japkutija.veterinarybackend.veterinary.model.entity.RefreshToken;
import com.Japkutija.veterinarybackend.veterinary.repository.RefreshTokenRepository;
import com.Japkutija.veterinarybackend.veterinary.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private static final Logger log = LoggerFactory.getLogger(RefreshTokenServiceImpl.class);
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

    }

    /*@Override
    public void saveRefreshToken(String username, String refreshToken) {
        var refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseGet(() -> {
                    var newRefreshToken = new RefreshToken();
                    newRefreshToken.setToken(refreshToken);
                    return newRefreshToken;
                });

        refreshTokenEntity.setUser(username);
        refreshTokenRepository.save(refreshTokenEntity);

    }*/
}
