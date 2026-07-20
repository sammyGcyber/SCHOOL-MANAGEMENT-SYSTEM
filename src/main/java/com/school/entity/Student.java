package com.school.entity;
import jakarta.persistence.*;
import java.time.LocalDate;
@Entity public class Student extends BaseEntity {
 @Column(nullable=false,unique=true) private String registrationNumber; @Column(nullable=false) private String firstName; @Column(nullable=false) private String lastName; @Column(nullable=false,unique=true) private String email; private String phone; private LocalDate admissionDate=LocalDate.now(); @ManyToOne private Programme programme; @OneToOne private User user;
 protected Student(){} public Student(String reg,String first,String last,String email){registrationNumber=reg;firstName=first;lastName=last;this.email=email;}
 public String getRegistrationNumber(){return registrationNumber;} public String getFirstName(){return firstName;} public String getLastName(){return lastName;} public String getEmail(){return email;} public String getPhone(){return phone;} public Programme getProgramme(){return programme;} public void setPhone(String p){phone=p;} public void setProgramme(Programme p){programme=p;}
}
