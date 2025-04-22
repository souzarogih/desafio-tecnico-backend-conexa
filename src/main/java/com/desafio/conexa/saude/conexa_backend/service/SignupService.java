package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.SignupRequestDTO;
import com.desafio.conexa.saude.conexa_backend.enums.UserStatus;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.model.UserPassword;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import com.desafio.conexa.saude.conexa_backend.utils.DateUtils;
import com.desafio.conexa.saude.conexa_backend.utils.UuidGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
@RequiredArgsConstructor
public class SignupService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public String signup(SignupRequestDTO signupRequest) {
        log.info("Processando o cadastro do usuário {}", signupRequest.email());

        userRepository.findByEmail(signupRequest.email()).ifPresent(user -> {
            log.error("E-mail já está cadastrado!");
            throw new IllegalArgumentException("E-mail já está cadastrado!");
        });

        userRepository.findByCpfNumber(signupRequest.cpfNumber()).ifPresent(user -> {
            log.error("CPF já está cadastrado!");
            throw new IllegalArgumentException("CPF já está cadastrado!");
        });

        if (!signupRequest.password().equals(signupRequest.confirmationPassword())) {
            log.error("As senhas não coincidem.");
            throw new IllegalArgumentException("As senhas não coincidem.");
        }

        User user = User.builder()
                .id(UuidGeneratorUtils.generateString())
                .email(signupRequest.email())
                .medicalSpecialty(signupRequest.medicalSpecialty())
                .cpfNumber(signupRequest.cpfNumber())
                .dateBirth(signupRequest.dateBirth())
                .phoneNumber(signupRequest.phoneNumber())
                .role(signupRequest.role())
                .userStatus(UserStatus.PENDING)
                .build();

        UserPassword userPassword = new UserPassword(
                UuidGeneratorUtils.generateString(),
                user,
                passwordEncoder.encode(signupRequest.password()),
                true,
                false,
                DateUtils.threeMonthsLater(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        user.setUserPassword(userPassword);

        userRepository.save(user);

        return "Cadastro com email "+user.getEmail() + " realizado com sucesso!";
    }
}
