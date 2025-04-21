package com.desafio.conexa.saude.conexa_backend.repository;

import com.desafio.conexa.saude.conexa_backend.model.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, String> {
}
