package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.enums.Role;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.model.UserPassword;
import com.desafio.conexa.saude.conexa_backend.repository.SignupRepository;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private SignupRepository signupRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id("123")
                .email("user@example.com")
                .role(Role.DOCTOR)
                .build();

        UserPassword userPassword = new UserPassword();
        userPassword.setPassword("encodedPassword");
        user.setUserPassword(userPassword);
    }

    @Test
    @DisplayName("Should return user details if username exists")
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {

        when(signupRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        UserDetails userDetails = userService.loadUserByUsername("user@example.com");
        assertNotNull(userDetails);
        assertEquals("user@example.com", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_DOCTOR")));
    }

    @Test
    @DisplayName("Should throw exception if username is not found")
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {

        when(signupRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Should return user when email exists")
    void findByEmail_shouldReturnUserOptional_whenUserExists() {

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        Optional<User> result = userService.findByEmail("user@example.com");
        assertTrue(result.isPresent());
        assertEquals("user@example.com", result.get().getEmail());
    }

    @Test
    @DisplayName("Should return empty when user doesn't exist")
    void findByEmail_shouldReturnEmptyOptional_whenUserDoesNotExist() {

        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());
        Optional<User> result = userService.findByEmail("missing@example.com");
        assertTrue(result.isEmpty());
    }
}

