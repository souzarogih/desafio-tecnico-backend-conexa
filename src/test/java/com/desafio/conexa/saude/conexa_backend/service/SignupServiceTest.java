package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.SignupRequestDTO;
import com.desafio.conexa.saude.conexa_backend.enums.Role;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SignupServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private SignupService signupService;

    private SignupRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new SignupRequestDTO(
                "user@example.com",
                "senha123",
                "senha123",
                "Cardiologia",
                "01234567890",
                LocalDate.of(1990, 1, 1).toString(),
                "+55 11 91234-5678",
                Role.DOCTOR
        );
    }

    @Test
    @DisplayName("Should register successfully with a valid request")
    void signup_shouldRegisterSuccessfully_whenValidRequest() {

        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
        when(userRepository.findByCpfNumber(validRequest.cpfNumber())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(validRequest.password())).thenReturn("encoded-password");
        String esperada = "Cadastro com email "+ validRequest.email() + " realizado com sucesso!";
        String response = signupService.signup(validRequest);

        assertTrue(response.contains(esperada));
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void signup_shouldThrowException_whenEmailAlreadyExists() {

        when(userRepository.findByEmail(validRequest.email()))
                .thenReturn(Optional.of(mock(User.class)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> signupService.signup(validRequest));

        assertEquals("E-mail já está cadastrado!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when CPF already exists")
    void signup_shouldThrowException_whenCpfAlreadyExists() {

        when(userRepository.findByEmail(validRequest.email())).thenReturn(Optional.empty());
        when(userRepository.findByCpfNumber(validRequest.cpfNumber()))
                .thenReturn(Optional.of(mock(User.class)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> signupService.signup(validRequest));

        assertEquals("CPF já está cadastrado!", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw exception when passwords don't match")
    void signup_shouldThrowException_whenPasswordsDoNotMatch() {

        SignupRequestDTO invalidPasswordRequest = new SignupRequestDTO(
                validRequest.email(),
                "senha123",
                "senha123",
                validRequest.medicalSpecialty(),
                validRequest.cpfNumber(),
                validRequest.dateBirth(),
                validRequest.phoneNumber(),
                validRequest.role()
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> signupService.signup(invalidPasswordRequest));

        assertEquals("As senhas não coincidem.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}