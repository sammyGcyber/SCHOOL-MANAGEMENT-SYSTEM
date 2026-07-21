package com.school.service.impl;

import com.school.dto.StudentForm;
import com.school.entity.Student;
import com.school.entity.User;
import com.school.enums.RoleName;
import com.school.exception.ResourceNotFoundException;
import com.school.repository.StudentRepository;
import com.school.repository.UserRepository;
import com.school.service.StudentService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {
    private final StudentRepository students;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public StudentServiceImpl(StudentRepository students, UserRepository users, PasswordEncoder passwordEncoder) {
        this.students = students;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Student> search(String q) {
        return q == null || q.isBlank()
                ? students.findAll()
                : students.findByRegistrationNumberContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(q, q, q);
    }

    public Student get(Long id) {
        return students.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found"));
    }

    public Student create(StudentForm f) {
        String email = f.getEmail();
        String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        if (users.existsByUsername(username)) {
            username = f.getRegistrationNumber().toLowerCase().replace('/', '-');
        }

        final String finalUsername = username;
        User user = users.findByUsername(finalUsername).orElseGet(() -> {
            User u = new User(finalUsername, email, passwordEncoder.encode("Student@123"), f.getFirstName() + " " + f.getLastName(), RoleName.STUDENT);
            return users.save(u);
        });

        Student s = new Student(f.getRegistrationNumber(), f.getFirstName(), f.getLastName(), f.getEmail());
        s.setUser(user);
        s.setPhone(f.getPhone());
        return students.save(s);
    }

    public void delete(Long id) {
        Student s = get(id);
        User u = s.getUser();
        students.delete(s);
        if (u != null) {
            users.delete(u);
        }
    }
}
