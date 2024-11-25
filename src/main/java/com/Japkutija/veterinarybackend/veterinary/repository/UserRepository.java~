package com.Japkutija.veterinarybackend.veterinary.repository;

import com.Japkutija.veterinarybackend.veterinary.model.entity.Appointment;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
