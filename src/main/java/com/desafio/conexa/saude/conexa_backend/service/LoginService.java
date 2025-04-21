package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.LoginRequest;
import com.desafio.conexa.saude.conexa_backend.dto.LoginResponse;
import com.desafio.conexa.saude.conexa_backend.dto.UserDTO;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public LoginResponse login(LoginRequest request) {
        log.info("Processando o login do email: {}", request.email());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        log.info("Usuário existe: {} - email: {}", user.getId(), user.getEmail());

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserPassword().getUser().getEmail())
                .password(user.getUserPassword().getPassword())
                .roles(user.getRole().name())
                .build();

        String token = jwtService.generateToken(userDetails);

        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        return new LoginResponse(token);
    }

    public UserDTO getLoggedLInUser(String email) {
        log.info("Consultando dados do usuário: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getMedicalSpecialty(),
                user.getCpfNumber(),
                user.getPhoneNumber(),
                user.getRole(),
                user.getUserStatus(),
                user.getLastLoginAt(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
