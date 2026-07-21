package com.school.repository;

import com.school.entity.*;
import java.time.LocalDate;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
 List<Attendance> findByUnitOrderByAttendanceDateDesc(Unit unit);
 List<Attendance> findByUnitAndAttendanceDate(Unit unit, LocalDate date);

 @Modifying
 @Transactional
 @Query("DELETE FROM Attendance a WHERE a.unit = :unit AND a.attendanceDate = :date")
 void deleteByUnitAndAttendanceDate(@Param("unit") Unit unit, @Param("date") LocalDate date);
}

