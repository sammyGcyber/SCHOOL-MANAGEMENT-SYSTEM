package com.school.entity;
import jakarta.persistence.*;
@Entity public class Lecturer extends BaseEntity { @Column(nullable=false,unique=true) private String staffNumber; @Column(nullable=false) private String fullName; @Column(nullable=false,unique=true) private String email; @ManyToOne private Department department; @OneToOne private User user; protected Lecturer(){} public Lecturer(String s,String n,String e){staffNumber=s;fullName=n;email=e;} public String getFullName(){return fullName;} }
