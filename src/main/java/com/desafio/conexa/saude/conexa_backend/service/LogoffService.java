package com.desafio.conexa.saude.conexa_backend.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class LogoffService {

    private final RevokedTokensService revokedTokensService;

    public boolean logoff(String token) {
        log.info("Iniciando a revogação do token");
       revokedTokensService.revokeToken(token);
       return true;
    }
}
