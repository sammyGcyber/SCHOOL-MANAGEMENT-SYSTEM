package com.school.repository;
import com.school.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface EnrollmentRepository extends JpaRepository<Enrollment,Long>{ List<Enrollment> findByStudentOrderByCreatedAtDesc(Student student); boolean existsByStudentAndUnit(Student student,Unit unit); long countByUnit(Unit unit); List<Enrollment> findByUnit(Unit unit); }
