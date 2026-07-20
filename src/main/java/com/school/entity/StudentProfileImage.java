package com.school.entity;

import jakarta.persistence.*;

@Entity
@Table(name="student_profile_images")
public class StudentProfileImage extends BaseEntity {
 @OneToOne(optional=false) @JoinColumn(name="student_id",unique=true) private Student student;
 @Lob @Column(nullable=false,columnDefinition="LONGBLOB") private byte[] data;
 @Column(nullable=false) private String contentType;
 protected StudentProfileImage(){}
 public StudentProfileImage(Student student,byte[] data,String contentType){this.student=student;this.data=data;this.contentType=contentType;}
 public Student getStudent(){return student;} public byte[] getData(){return data;} public String getContentType(){return contentType;}
 public void replace(byte[] value,String type){data=value;contentType=type;}
}
