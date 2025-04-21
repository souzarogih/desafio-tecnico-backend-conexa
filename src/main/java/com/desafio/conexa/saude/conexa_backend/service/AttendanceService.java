package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.AttendanceRequest;
import com.desafio.conexa.saude.conexa_backend.dto.AttendanceResponse;
import com.desafio.conexa.saude.conexa_backend.model.Attendance;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.repository.AttendanceRepository;
import com.desafio.conexa.saude.conexa_backend.utils.UuidGeneratorUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Log4j2
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    public AttendanceResponse create(AttendanceRequest attendanceRequest, String email){
        log.info("Processando o agendamento do paciente: {}", attendanceRequest.patient().getName());

        User user = userService.findByEmail(email)
                .orElseThrow(() -> {
                    log.info("User não encontrado");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "User não encontrado");
                });

        Attendance attendance = attendanceRepository.save(
                new Attendance(
                        UuidGeneratorUtils.generateString(),
                        attendanceRequest.appointmentDateTime(),
                        attendanceRequest.patient(),
                        user));

        return new AttendanceResponse(
            attendance.getId(),
            attendance.getAppointmentDateTime(),
            attendance.getPatient()
        );
    }

    public AttendanceResponse findById(String attendanceId){
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> {
                    log.info("Agendamento não encontrado");
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Agendamento não encontrado");
                });

        return new AttendanceResponse(
                attendance.getId(),
                attendance.getAppointmentDateTime(),
                attendance.getPatient()
        );
    }
}
