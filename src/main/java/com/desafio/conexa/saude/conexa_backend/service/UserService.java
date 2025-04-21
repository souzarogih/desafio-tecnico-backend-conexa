package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.repository.SignupRepository;
import com.desafio.conexa.saude.conexa_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final SignupRepository signupRepository;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return signupRepository.findByEmail(username)
                .map(usuario -> org.springframework.security.core.userdetails.User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getUserPassword().getPassword())
                        .roles(usuario.getRole().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));
    }

    public void checkIfUserExistsByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email já cadastrado");
        }
    }
}
