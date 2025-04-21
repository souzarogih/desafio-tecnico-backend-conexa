package com.desafio.conexa.saude.conexa_backend.controller;

import com.desafio.conexa.saude.conexa_backend.dto.SignupRequestDTO;
import com.desafio.conexa.saude.conexa_backend.service.SignupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/signup")
public class SignupController {

    @Autowired
    SignupService signupService;

    @PostMapping
    public ResponseEntity<String> signup(@Valid  @RequestBody SignupRequestDTO signupRequestDTO){
        return ResponseEntity.ok(signupService.signup(signupRequestDTO));
    }
}
