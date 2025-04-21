package com.desafio.conexa.saude.conexa_backend.dto;

import com.desafio.conexa.saude.conexa_backend.enums.Role;
import com.desafio.conexa.saude.conexa_backend.enums.UserStatus;
import com.desafio.conexa.saude.conexa_backend.model.UserPassword;

import java.time.LocalDateTime;

public record UserDTO(
        String id,
        String email,
        String medicalSpecialty,
        String cpfNumber,
        String phoneNumber,
        Role role,
        UserStatus userStatus,
        LocalDateTime lastLoginAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
