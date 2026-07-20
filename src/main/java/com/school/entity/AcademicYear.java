package com.school.entity;
import jakarta.persistence.*;
@Entity public class AcademicYear extends BaseEntity { @Column(nullable=false,unique=true) private String name; private boolean currentYear; protected AcademicYear(){} public AcademicYear(String n,boolean c){name=n;currentYear=c;} public String getName(){return name;} }
