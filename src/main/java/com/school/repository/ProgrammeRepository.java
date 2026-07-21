package com.school.repository;

import com.school.entity.Programme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgrammeRepository extends JpaRepository<Programme, Long> {
}
