package com.school.repository;
import com.school.entity.Student; import java.util.List; import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student,Long>{ List<Student> findByRegistrationNumberContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String q1,String q2,String q3); }
