package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.LoginRequest;
import com.desafio.conexa.saude.conexa_backend.dto.LoginResponse;
import com.desafio.conexa.saude.conexa_backend.dto.UserDTO;
import com.desafio.conexa.saude.conexa_backend.enums.Role;
import com.desafio.conexa.saude.conexa_backend.enums.UserStatus;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.model.UserPassword;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    private User user;
    private LoginRequest loginRequest;

    @BeforeEach
    void setup() {
        loginRequest = new LoginRequest("user@example.com", "password");

        user = new User();
        user.setId("user123");
        user.setEmail("user@example.com");

        user.setRole(Role.USER);
        user.setLastLoginAt(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setUserStatus(UserStatus.ACTIVATED);
        user.setCpfNumber("12345678900");
        user.setPhoneNumber("11999999999");
        user.setMedicalSpecialty("Cardiologia");

        UserPassword userPassword = new UserPassword();
        userPassword.setPassword("hashedPassword");
        userPassword.setUser(user);

        user.setUserPassword(userPassword);
    }

    @Test
    void login_shouldReturnToken_whenCredentialsAreValid() {
        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt-token");

        LoginResponse response = loginService.login(loginRequest);

        assertNotNull(response);
        assertEquals("jwt-token", response.token());
        verify(userRepository).save(user);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowException_whenUserNotFound() {

        when(userRepository.findByEmail(loginRequest.email())).thenReturn(Optional.empty());
        doAnswer(invocation -> null).when(authenticationManager).authenticate(any());
        assertThrows(UsernameNotFoundException.class, () -> loginService.login(loginRequest));
    }

    @Test
    void getLoggedInUser_shouldReturnUserDTO_whenUserExists() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        UserDTO result = loginService.getLoggedLInUser(user.getEmail());
        assertNotNull(result);
        assertEquals(user.getId(), result.id());
        assertEquals(user.getEmail(), result.email());
    }

    @Test
    void getLoggedInUser_shouldThrowException_whenUserDoesNotExist() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> loginService.getLoggedLInUser("nonexistent@example.com"));
    }
}
