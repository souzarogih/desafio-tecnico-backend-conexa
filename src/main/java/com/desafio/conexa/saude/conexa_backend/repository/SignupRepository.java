package com.desafio.conexa.saude.conexa_backend.repository;

import com.desafio.conexa.saude.conexa_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SignupRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
