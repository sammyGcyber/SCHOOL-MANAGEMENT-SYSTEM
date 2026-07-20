package com.school.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseEntity {
 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
 @Column(nullable=false, updatable=false) private LocalDateTime createdAt = LocalDateTime.now();
 private LocalDateTime updatedAt;
 @PreUpdate void touch(){ updatedAt=LocalDateTime.now(); }
 public Long getId(){return id;} public LocalDateTime getCreatedAt(){return createdAt;}
}
