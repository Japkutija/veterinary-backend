package com.Japkutija.veterinarybackend.veterinary.config;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**").permitAll()  // Allow all requests including Swagger
                .anyRequest().authenticated()
                .and()
                .csrf().disable();  // Disable CSRF protection as an example
    }
}