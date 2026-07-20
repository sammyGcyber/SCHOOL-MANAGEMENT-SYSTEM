package com.school.repository;
import com.school.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface LecturerProfileImageRepository extends JpaRepository<LecturerProfileImage,Long>{ Optional<LecturerProfileImage> findByLecturer(Lecturer lecturer); boolean existsByLecturer(Lecturer lecturer); }
