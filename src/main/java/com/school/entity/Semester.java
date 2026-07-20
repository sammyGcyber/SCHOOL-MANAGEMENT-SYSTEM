package com.school.entity;
import jakarta.persistence.*;
@Entity public class Semester extends BaseEntity { @Column(nullable=false) private String name; private boolean currentSemester; @ManyToOne(optional=false) private AcademicYear academicYear; protected Semester(){} public Semester(String n,AcademicYear y){name=n;academicYear=y;} public String getName(){return name;} }
