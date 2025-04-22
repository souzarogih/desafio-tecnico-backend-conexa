package com.desafio.conexa.saude.conexa_backend.model;

import com.desafio.conexa.saude.conexa_backend.enums.Role;
import com.desafio.conexa.saude.conexa_backend.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
@Builder
public class User {

    @Id
    private String id;

    @Column(name = "email", nullable = false, length = 100, unique = true)
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private UserPassword userPassword;

    @Column(name = "medical_specialty", nullable = false, length = 50)
    private String medicalSpecialty;

    @Column(name = "cpf_number", nullable = false, length = 11)
    private String cpfNumber;

    @Column(name = "date_birth", nullable = false, length = 10)
    private String dateBirth;

    @Column(name = "phone_number", nullable = false, length = 13)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
