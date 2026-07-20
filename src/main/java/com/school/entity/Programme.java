package com.school.entity;
import jakarta.persistence.*;
@Entity public class Programme extends BaseEntity { @Column(nullable=false,unique=true) private String code; @Column(nullable=false) private String name; @ManyToOne(optional=false) private Department department; protected Programme(){} public Programme(String c,String n,Department d){code=c;name=n;department=d;} public String getCode(){return code;} public String getName(){return name;} }
