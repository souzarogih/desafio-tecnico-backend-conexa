package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.model.RevokedTokens;
import com.desafio.conexa.saude.conexa_backend.repository.RevokedTokensRepository;
import com.desafio.conexa.saude.conexa_backend.utils.UuidGeneratorUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Log4j2
@Service
public class RevokedTokensService {

    @Autowired
    RevokedTokensRepository revokedTokensRepository;

    public void revokeToken(String token) {
        revokedTokensRepository.save(
                new RevokedTokens(
                        UuidGeneratorUtils.generateString(),
                        token,
                        LocalDateTime.now()
                )
        );
        log.info("Token revogado!");
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokensRepository.existsByToken(token);
    }

}
