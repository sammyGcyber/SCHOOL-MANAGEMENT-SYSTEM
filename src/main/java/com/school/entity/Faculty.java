package com.school.entity;
import jakarta.persistence.*;
@Entity public class Faculty extends BaseEntity { @Column(nullable=false,unique=true) private String name; protected Faculty(){} public Faculty(String name){this.name=name;} public String getName(){return name;} }
