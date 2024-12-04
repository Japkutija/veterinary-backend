package com.Japkutija.veterinarybackend.veterinary.service;

import com.Japkutija.veterinarybackend.veterinary.exception.EntityNotFoundException;
import com.Japkutija.veterinarybackend.veterinary.exception.UserAlreadyExistsException;
import com.Japkutija.veterinarybackend.veterinary.model.entity.Owner;
import com.Japkutija.veterinarybackend.veterinary.model.entity.User;
import com.Japkutija.veterinarybackend.veterinary.model.enums.Role;
import com.Japkutija.veterinarybackend.veterinary.repository.OwnerRepository;
import com.Japkutija.veterinarybackend.veterinary.repository.UserRepository;
import com.Japkutija.veterinarybackend.veterinary.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OwnerRepository ownerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, ownerRepository);
    }

    @Test
    void registerUser_Success() {
        // Arrange
        var username = "testuser";
        var email = "test@example.com";
        var password = "password123";
        var firstName = "John";
        var lastName = "Doe";
        var dateOfBirth = LocalDate.of(1990, 1, 1);
        var emso = "1234567890123";
        var phoneNumber = "123456789";
        var address = "Test Address";

        when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(ownerRepository.existsByEMSO(anyString())).thenReturn(false);
        when(ownerRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(ownerRepository.save(any(Owner.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = userService.registerUser(username, email, password, firstName, lastName,
            dateOfBirth, emso, phoneNumber, address);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals(Role.USER, result.getRole());

        verify(userRepository).save(any(User.class));
        verify(ownerRepository).save(any(Owner.class));
    }

    @Test
    void registerUser_DuplicateUsername_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () ->
            userService.registerUser("existinguser", "test@example.com", "password",
                "John", "Doe", LocalDate.now(), "1234567890123", "123456789", "address"));

        verify(userRepository, never()).save(any(User.class));
        verify(ownerRepository, never()).save(any(Owner.class));
    }

    @Test
    void registerUser_DuplicateEmail_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () ->
            userService.registerUser("newuser", "existing@example.com", "password", 
                "John", "Doe", LocalDate.now(), "1234567890123", "123456789", "address"));

        verify(userRepository, never()).save(any(User.class));
        verify(ownerRepository, never()).save(any(Owner.class));
    }

    @Test
    void registerUser_DuplicateEMSO_ThrowsException() {
        // Arrange
        when(userRepository.existsByUsernameIgnoreCase(anyString())).thenReturn(false);
        when(userRepository.existsByEmailIgnoreCase(anyString())).thenReturn(false);
        when(ownerRepository.existsByEMSO(anyString())).thenReturn(true);

        // Act & Assert
        assertThrows(UserAlreadyExistsException.class, () ->
            userService.registerUser("newuser", "test@example.com", "password", 
                "John", "Doe", LocalDate.now(), "1234567890123", "123456789", "address"));

        verify(userRepository, never()).save(any(User.class));
        verify(ownerRepository, never()).save(any(Owner.class));
    }

    @Test
    void findByUsername_Success() {
        // Arrange
        var username = "testuser";
        var mockUser = new User();
        mockUser.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // Act
        var result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_UserNotFound_ThrowsException() {
        // Arrange
        var username = "nonexistent";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userService.findByUsername(username));
        verify(userRepository).findByUsername(username);
    }
}
