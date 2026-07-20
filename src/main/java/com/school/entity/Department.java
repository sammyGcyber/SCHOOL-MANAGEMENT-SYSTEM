package com.school.entity;
import jakarta.persistence.*;
@Entity public class Department extends BaseEntity { @Column(nullable=false) private String name; @ManyToOne(optional=false) private Faculty faculty; protected Department(){} public Department(String n,Faculty f){name=n;faculty=f;} public String getName(){return name;} }
