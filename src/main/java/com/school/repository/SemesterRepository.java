package com.school.repository;
import com.school.entity.Semester; import org.springframework.data.jpa.repository.JpaRepository;
public interface SemesterRepository extends JpaRepository<Semester,Long>{}
