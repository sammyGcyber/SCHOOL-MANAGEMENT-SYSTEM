package com.school.repository;

import com.school.entity.AdminProfileImage;
import com.school.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminProfileImageRepository extends JpaRepository<AdminProfileImage, Long> {
    Optional<AdminProfileImage> findByUser(User user);
    boolean existsByUser(User user);
}
