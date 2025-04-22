package com.desafio.conexa.saude.conexa_backend.dto;

import com.desafio.conexa.saude.conexa_backend.model.Patient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record AttendanceResponse(
        String id,
        LocalDateTime appointmentDateTime,
        Patient patient,
        LocalDate appointmentDate,
        LocalTime appointmentTime
) {
}
