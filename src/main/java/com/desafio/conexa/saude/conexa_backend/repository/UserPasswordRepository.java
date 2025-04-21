package com.desafio.conexa.saude.conexa_backend.repository;

import com.desafio.conexa.saude.conexa_backend.model.UserPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordRepository extends JpaRepository<UserPassword, String> {
}
