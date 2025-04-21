package com.desafio.conexa.saude.conexa_backend.controller;

import com.desafio.conexa.saude.conexa_backend.service.LogoffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/logoff")
public class LogoffController {

    @Autowired
    LogoffService logoffService;

    @PostMapping
    public ResponseEntity<?> logoff(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.replace("Bearer ", "");
        logoffService.logoff(token);
        return ResponseEntity.ok("Logout efetuado com sucesso");
    }
}
