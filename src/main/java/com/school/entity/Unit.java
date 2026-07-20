package com.school.entity;
import jakarta.persistence.*;
@Entity @Table(name="units") public class Unit extends BaseEntity { @Column(nullable=false,unique=true) private String code; @Column(nullable=false) private String name; private int creditHours=3; @ManyToOne private Programme programme; @ManyToOne private Lecturer lecturer; protected Unit(){} public Unit(String c,String n){code=c;name=n;} public String getCode(){return code;} public String getName(){return name;} public int getCreditHours(){return creditHours;} }
