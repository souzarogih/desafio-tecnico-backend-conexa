package com.desafio.conexa.saude.conexa_backend.controller;

import com.desafio.conexa.saude.conexa_backend.dto.AttendanceRequest;
import com.desafio.conexa.saude.conexa_backend.dto.AttendanceResponse;
import com.desafio.conexa.saude.conexa_backend.service.AttendanceService;
import com.desafio.conexa.saude.conexa_backend.service.JwtService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Log4j2
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    AttendanceService attendanceService;

    @Autowired
    JwtService jwtService;

    @PostMapping
    public ResponseEntity<AttendanceResponse> create(@RequestHeader("Authorization") String authHeader,
                                                    @RequestBody AttendanceRequest attendanceRequest){
        log.info("Recebendo requisição para agendar uma consulta.");

        String token = authHeader.replace("Bearer ", "");
        String email = jwtService.extractUsername(token);

        return ResponseEntity.ok(attendanceService.create(attendanceRequest, email));
    }

    @GetMapping("/{attendanceId}")
    public ResponseEntity<AttendanceResponse> findById(@PathVariable String attendanceId){
        return ResponseEntity.ok(attendanceService.findById(attendanceId));
    }

    @GetMapping("/appointments/{appointmentDateTime}")
    public ResponseEntity<List<AttendanceResponse>> findAllByAppointmentDateTime(@PathVariable LocalDate appointmentDateTime){
        log.info("Consultado agendamentos para o dia {}", appointmentDateTime);
        return ResponseEntity.ok(attendanceService.findAllByAppointmentDateTime(appointmentDateTime));
    }

    @PatchMapping("/attendances/{attendanceId}/complete")
    public ResponseEntity<AttendanceResponse> enableMedicalConsultation(@PathVariable String attendanceId){
        log.info("Recebendo requisição para realizar o atendimento de uma consulta.");

        return ResponseEntity.ok(attendanceService.carryOutMedicalConsultationService(attendanceId));
    }
}
