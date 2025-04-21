package com.desafio.conexa.saude.conexa_backend.dto;

import com.desafio.conexa.saude.conexa_backend.model.Patient;

import java.time.LocalDateTime;

public record AttendanceResponse(
        String id,
        LocalDateTime appointmentDateTime,
        Patient patient
) {
}
