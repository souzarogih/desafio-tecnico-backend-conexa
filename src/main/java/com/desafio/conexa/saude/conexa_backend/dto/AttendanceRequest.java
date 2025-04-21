package com.desafio.conexa.saude.conexa_backend.dto;

import com.desafio.conexa.saude.conexa_backend.model.Patient;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AttendanceRequest(

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime appointmentDateTime,

        Patient patient,
        String userId
) {
}
