package com.school.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity public class Student extends BaseEntity {
 @Column(nullable=false,unique=true) private String registrationNumber; @Column(nullable=false) private String firstName; @Column(nullable=false) private String lastName; @Column(nullable=false,unique=true) private String email; private String phone; private LocalDate admissionDate=LocalDate.now(); @Lob @Column(columnDefinition="LONGBLOB") private byte[] profileImage; private String profileImageContentType; @ManyToOne private Programme programme; @OneToOne private User user;
 protected Student(){} public Student(String reg,String first,String last,String email){registrationNumber=reg;firstName=first;lastName=last;this.email=email;}
 public String getRegistrationNumber(){return registrationNumber;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getEmail(){return email;} public String getPhone(){return phone;} public Programme getProgramme(){return programme;} public User getUser(){return user;} public LocalDate getAdmissionDate(){return admissionDate;} public byte[] getProfileImage(){return profileImage;} public String getProfileImageContentType(){return profileImageContentType;} public boolean hasProfileImage(){return profileImage!=null&&profileImage.length>0;}
 public void setPhone(String p){phone=p;} public void setEmail(String e){email=e;} public void setProgramme(Programme p){programme=p;} public void setUser(User u){user=u;} public void setProfileImage(byte[] image,String contentType){profileImage=image;profileImageContentType=contentType;}
}
