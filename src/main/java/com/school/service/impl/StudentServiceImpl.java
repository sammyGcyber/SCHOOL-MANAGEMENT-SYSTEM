package com.school.service.impl;
import com.school.dto.StudentForm; import com.school.entity.Student; import com.school.exception.ResourceNotFoundException; import com.school.repository.StudentRepository; import com.school.service.StudentService; import java.util.List; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service @Transactional public class StudentServiceImpl implements StudentService { private final StudentRepository students; public StudentServiceImpl(StudentRepository students){this.students=students;}
 public List<Student> search(String q){return q==null||q.isBlank()?students.findAll():students.findByRegistrationNumberContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(q,q,q);}
 public Student get(Long id){return students.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found"));}
 public Student create(StudentForm f){Student s=new Student(f.getRegistrationNumber(),f.getFirstName(),f.getLastName(),f.getEmail());s.setPhone(f.getPhone());return students.save(s);} public void delete(Long id){students.delete(get(id));} }
