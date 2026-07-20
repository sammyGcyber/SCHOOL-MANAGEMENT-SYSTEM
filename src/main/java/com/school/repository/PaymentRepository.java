package com.school.repository;
import com.school.entity.*; import java.util.*; import org.springframework.data.jpa.repository.JpaRepository;
public interface PaymentRepository extends JpaRepository<Payment,Long>{ List<Payment> findByStudentOrderByPaymentDateDesc(Student student); }
