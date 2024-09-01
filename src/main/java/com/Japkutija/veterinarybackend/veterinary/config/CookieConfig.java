package com.Japkutija.veterinarybackend.veterinary.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class CookieConfig {

    @Value("${cookie.same-site}")
    private String sameSite;

    @Value("${cookie.secure}")
    private boolean secure;
}