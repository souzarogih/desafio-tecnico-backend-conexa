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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.desafio.conexa.saude.conexa_backend.constants.MessagesConstants.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserService userService;

    public AttendanceResponse create(AttendanceRequest attendanceRequest, String email){
        log.info("Processando o agendamento do paciente: {}", attendanceRequest.patient().getName());

        log.info(":{}", attendanceRequest.appointmentDateTime());
        log.info(":{}", LocalDateTime.now());
        if (!attendanceRequest.appointmentDateTime().isAfter(LocalDateTime.now())){
            log.error(APPOINTMENT_DATE_CANNOT_PAST);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, APPOINTMENT_DATE_CANNOT_PAST);
        }

        User user = userService.findByEmail(email)
                .orElseThrow(() -> {
                    log.error(USER_NOT_FOUND);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, USER_NOT_FOUND);
                });

        List<AttendanceResponse> attendanceResponseList = attendanceRepository.findAllByUserId(user.getId())
                .stream()
                .map(attendance -> new AttendanceResponse(
                        attendance.getId(), attendance.getAppointmentDateTime(), attendance.getPatient(), attendance.getAppointmentDate(),
                        attendance.getAppointmentTime()
                )).collect(Collectors.toList());

        boolean attendanceExists = attendanceResponseList.stream()
                .anyMatch(existingAttendance -> existingAttendance.appointmentDateTime().equals(attendanceRequest.appointmentDateTime()));

        if (attendanceExists) {
            log.error(SCHEDULE_ALREADY_EXISTS_FOR_DATE_TIME + " - " + attendanceRequest.appointmentDateTime());
            throw new ResponseStatusException(HttpStatus.CONFLICT, SCHEDULE_ALREADY_EXISTS_FOR_DATE_TIME);
        }

        Attendance attendance = attendanceRepository.save(
                new Attendance(
                        UuidGeneratorUtils.generateString(),
                        attendanceRequest.appointmentDateTime(),
                        attendanceRequest.appointmentDateTime().toLocalDate(),
                        attendanceRequest.appointmentDateTime().toLocalTime(),
                        attendanceRequest.patient(),
                        user,
                        false));

        return new AttendanceResponse(
            attendance.getId(),
            attendance.getAppointmentDateTime(),
            attendance.getPatient(),
                attendance.getAppointmentDate(),
                attendance.getAppointmentTime()
        );
    }

    public AttendanceResponse findById(String attendanceId){
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> {
                    log.error(APPOINTMENT_NOT_FOUND);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, APPOINTMENT_NOT_FOUND);
                });

        log.info("Consulta localizada com sucesso: id:{} paciente:{}", attendance.getId(), attendance.getPatient());
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getAppointmentDateTime(),
                attendance.getPatient(),
                attendance.getAppointmentDate(),
                attendance.getAppointmentTime()
        );
    }

    public List<AttendanceResponse> findAllByAppointmentDateTime(LocalDate appointmentDate){
        log.info("Realizando consulta de agendamento para uma data: {}", appointmentDate);
        List<AttendanceResponse> attendances = attendanceRepository.findAllByAppointmentDate(appointmentDate)
                .stream()
                .map(attendanceList -> new AttendanceResponse(
                        attendanceList.getId(), attendanceList.getAppointmentDateTime(), attendanceList.getPatient(), attendanceList.getAppointmentDate(), attendanceList.getAppointmentTime()
                )).collect(Collectors.toList());
        log.info("Retorando a lista de atendimentos para o dia informado.");

        return attendances;
    }

    public AttendanceResponse carryOutMedicalConsultationService(String attendanceId){
        log.info("Habiltiando a consulta como realizada");

        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> {
                    log.error(APPOINTMENT_NOT_FOUND);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, APPOINTMENT_NOT_FOUND);
                });

        if (!attendance.isServiceProvided()){
            attendance.setServiceProvided(true);
        }

        Attendance attendanceUpdated = attendanceRepository.save(attendance);
        log.info("Atendimento confirmado: {} em {}", attendanceUpdated.getPatient(), attendanceUpdated.getAppointmentDate());

        return new AttendanceResponse(
                attendanceUpdated.getId(),
                attendanceUpdated.getAppointmentDateTime(),
                attendanceUpdated.getPatient(),
                attendanceUpdated.getAppointmentDate(),
                attendanceUpdated.getAppointmentTime()
        );
    }
}