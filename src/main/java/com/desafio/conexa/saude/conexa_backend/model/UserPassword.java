package com.desafio.conexa.saude.conexa_backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users_password")
@Builder
public class UserPassword {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "confirmation_password", nullable = false)
    private boolean confirmationPassword;

    @Column(name = "force_rotation")
    private boolean forceRotation;

    @Column(name = "expires_in", nullable = false)
    private LocalDateTime expiresIn;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
