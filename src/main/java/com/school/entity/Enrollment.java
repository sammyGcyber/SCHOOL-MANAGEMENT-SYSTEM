package com.school.entity;
import jakarta.persistence.*;
@Entity public class Enrollment extends BaseEntity { @ManyToOne(optional=false) private Student student; @ManyToOne(optional=false) private Unit unit; @ManyToOne(optional=false) private Semester semester; protected Enrollment(){} public Enrollment(Student s,Unit u,Semester sem){student=s;unit=u;semester=sem;} public Student getStudent(){return student;} public Unit getUnit(){return unit;} public Semester getSemester(){return semester;} }
