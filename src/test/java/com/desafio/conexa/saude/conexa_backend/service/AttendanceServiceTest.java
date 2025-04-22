package com.desafio.conexa.saude.conexa_backend.service;

import com.desafio.conexa.saude.conexa_backend.dto.AttendanceRequest;
import com.desafio.conexa.saude.conexa_backend.dto.AttendanceResponse;
import com.desafio.conexa.saude.conexa_backend.model.Attendance;
import com.desafio.conexa.saude.conexa_backend.model.Patient;
import com.desafio.conexa.saude.conexa_backend.model.User;
import com.desafio.conexa.saude.conexa_backend.repository.AttendanceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @InjectMocks
    private AttendanceService attendanceService;

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private UserService userService;

    private final LocalDateTime FUTURE_DATE = LocalDateTime.now().plusDays(1);
    private final LocalDateTime PAST_DATE = LocalDateTime.now().minusDays(1);
    private final String EMAIL = "test@email.com";
    private final String USER_ID = "user123";
    private final String ATTENDANCE_ID = "att123";

    private final Patient PATIENT = new Patient("João", "111.111.111-11");
    private final User USER = User.builder().id(USER_ID).email(EMAIL).build();

    @Test
    @DisplayName("Should create attendance successfully")
    void shouldCreateAttendanceSuccessfully() {
        AttendanceRequest request = new AttendanceRequest(FUTURE_DATE, PATIENT);

        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        when(attendanceRepository.findAllByUserId(USER_ID)).thenReturn(List.of());
        when(attendanceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.create(request, EMAIL);

        assertEquals(request.appointmentDateTime(), response.appointmentDateTime());
        assertEquals(request.patient().getName(), response.patient().getName());
    }

    @Test
    @DisplayName("Should throw exception when date is in the past")
    void shouldThrowWhenDateIsInPast() {
        AttendanceRequest request = new AttendanceRequest(PAST_DATE, PATIENT);

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                attendanceService.create(request, EMAIL));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, ex.getStatusCode());
    }

    @Test
    @DisplayName("Should throw exception when schedule already exists")
    void shouldThrowWhenScheduleAlreadyExists() {
        AttendanceRequest request = new AttendanceRequest(FUTURE_DATE, PATIENT);
        Attendance existing = new Attendance("123", FUTURE_DATE, FUTURE_DATE.toLocalDate(), FUTURE_DATE.toLocalTime(), PATIENT, USER, false);

        when(userService.findByEmail(EMAIL)).thenReturn(Optional.of(USER));
        when(attendanceRepository.findAllByUserId(USER_ID)).thenReturn(List.of(existing));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                attendanceService.create(request, EMAIL));

        assertEquals(HttpStatus.CONFLICT, ex.getStatusCode());
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void shouldThrowWhenUserNotFound() {
        AttendanceRequest request = new AttendanceRequest(FUTURE_DATE, PATIENT);

        when(userService.findByEmail(EMAIL)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                attendanceService.create(request, EMAIL));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("Should find attendance by ID successfully")
    void shouldFindByIdSuccessfully() {
        Attendance attendance = new Attendance(ATTENDANCE_ID, FUTURE_DATE, FUTURE_DATE.toLocalDate(), FUTURE_DATE.toLocalTime(), PATIENT, USER, false);

        when(attendanceRepository.findById(ATTENDANCE_ID)).thenReturn(Optional.of(attendance));

        AttendanceResponse response = attendanceService.findById(ATTENDANCE_ID);

        assertEquals(ATTENDANCE_ID, response.id());
    }

    @Test
    @DisplayName("Should throw exception when attendance is not found")
    void shouldThrowWhenAttendanceNotFound() {
        when(attendanceRepository.findById(ATTENDANCE_ID)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                attendanceService.findById(ATTENDANCE_ID));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    @DisplayName("Should find all attendances by date")
    void shouldFindAllByDate() {
        LocalDate date = FUTURE_DATE.toLocalDate();
        Attendance att = new Attendance("1", FUTURE_DATE, date, FUTURE_DATE.toLocalTime(), PATIENT, USER, false);

        when(attendanceRepository.findAllByAppointmentDate(date)).thenReturn(List.of(att));

        List<AttendanceResponse> list = attendanceService.findAllByAppointmentDateTime(date);

        assertEquals(1, list.size());
    }

    @Test
    @DisplayName("Should carry out consultation successfully")
    void shouldCarryOutConsultationSuccessfully() {
        Attendance att = new Attendance(ATTENDANCE_ID, FUTURE_DATE, FUTURE_DATE.toLocalDate(), FUTURE_DATE.toLocalTime(), PATIENT, USER, false);

        when(attendanceRepository.findById(ATTENDANCE_ID)).thenReturn(Optional.of(att));
        when(attendanceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        AttendanceResponse response = attendanceService.carryOutMedicalConsultationService(ATTENDANCE_ID);

        assertTrue(response != null);
    }

    @Test
    @DisplayName("Should throw exception when trying to complete consultation with invalid ID")
    void shouldThrowWhenCarryingOutConsultationWithInvalidId() {
        when(attendanceRepository.findById(ATTENDANCE_ID)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () ->
                attendanceService.carryOutMedicalConsultationService(ATTENDANCE_ID));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
