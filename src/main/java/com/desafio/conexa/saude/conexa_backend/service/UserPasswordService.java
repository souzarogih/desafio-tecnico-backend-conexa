package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.model.UserPassword;
import com.desafio.conexa.saude.conexa_backend.repository.UserPasswordRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Log4j2
@Service
public class UserPasswordService {

    @Autowired
    UserPasswordRepository userPasswordRepository;

    public UserPassword create(UserPassword userPassword){
        return userPasswordRepository.save(userPassword);
    }

    public UserPassword findById(String userPasswordId){
        return userPasswordRepository.findById(userPasswordId)
                .orElseThrow(() -> {
                                    log.error("Usuário não encontrado com id: {}", userPasswordId);
                                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
                                });
    }
}
