package com.school.service;
import com.school.dto.StudentForm; import com.school.entity.Student; import java.util.List;
public interface StudentService { List<Student> search(String query); Student get(Long id); Student create(StudentForm form); void delete(Long id); }
