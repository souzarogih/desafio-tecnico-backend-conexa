package com.desafio.conexa.saude.conexa_backend.controller;

import com.desafio.conexa.saude.conexa_backend.dto.LoginRequest;
import com.desafio.conexa.saude.conexa_backend.dto.LoginResponse;
import com.desafio.conexa.saude.conexa_backend.dto.UserDTO;
import com.desafio.conexa.saude.conexa_backend.service.LoginService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    LoginService loginService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok(loginService.login(loginRequest));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        log.info("Recebendo requisição para buscar dados de um usuário");

        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("Error na validação do login");
            return ResponseEntity.status(401).build();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return ResponseEntity.ok(loginService.getLoggedLInUser(userDetails.getUsername()));
        }

        return ResponseEntity.status(401).build();
    }
}
