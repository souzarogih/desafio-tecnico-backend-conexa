package com.desafio.conexa.saude.conexa_backend.repository;

import com.desafio.conexa.saude.conexa_backend.model.RevokedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevokedTokensRepository extends JpaRepository<RevokedTokens, String> {
    Optional<RevokedTokens> findByToken(String token);

    boolean existsByToken(String token);
}
