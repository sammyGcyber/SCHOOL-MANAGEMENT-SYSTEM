package com.school.entity;
import jakarta.persistence.*;
@Entity public class Announcement extends BaseEntity { @Column(nullable=false) private String title; @Column(length=3000,nullable=false) private String content; private boolean published=true; protected Announcement(){} public Announcement(String t,String c){title=t;content=c;} public String getTitle(){return title;} public String getContent(){return content;} }
