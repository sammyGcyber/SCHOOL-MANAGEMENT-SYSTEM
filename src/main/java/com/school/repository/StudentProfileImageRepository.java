package com.school.repository;

import com.school.entity.*; import java.util.Optional; import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentProfileImageRepository extends JpaRepository<StudentProfileImage,Long>{ Optional<StudentProfileImage> findByStudent(Student student); boolean existsByStudent(Student student); }
