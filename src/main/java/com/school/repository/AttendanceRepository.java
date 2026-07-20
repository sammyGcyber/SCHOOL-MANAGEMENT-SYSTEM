package com.school.repository;
import com.school.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface AttendanceRepository extends JpaRepository<Attendance,Long>{ List<Attendance> findByUnitOrderByAttendanceDateDesc(Unit unit); }
