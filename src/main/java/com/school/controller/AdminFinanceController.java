package com.school.controller;

import com.school.entity.Payment;
import com.school.entity.Student;
import com.school.repository.PaymentRepository;
import com.school.repository.StudentRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/finance")
public class AdminFinanceController {
    private final PaymentRepository payments;
    private final StudentRepository students;

    public AdminFinanceController(PaymentRepository payments, StudentRepository students) {
        this.payments = payments;
        this.students = students;
    }

    @GetMapping
    public String financeView(Model model) {
        List<Payment> list = payments.findAll().stream()
                .sorted((a, b) -> b.getPaymentDate().compareTo(a.getPaymentDate()))
                .toList();

        BigDecimal totalCollected = list.stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("active", "finance");
        model.addAttribute("payments", list);
        model.addAttribute("totalCollected", totalCollected);
        return "admin/finance";
    }

    @GetMapping("/new")
    public String paymentForm(Model model) {
        model.addAttribute("active", "finance");
        model.addAttribute("students", students.findAll());
        return "admin/payment-form";
    }

    @PostMapping
    @Transactional
    public String recordPayment(@RequestParam Long studentId, @RequestParam BigDecimal amount, RedirectAttributes flash) {
        Student s = students.findById(studentId).orElseThrow();
        String receipt = "REC-" + (System.currentTimeMillis() % 1_000_000);
        Payment payment = new Payment(s, amount, receipt);
        payments.save(payment);
        flash.addFlashAttribute("success", "Payment of KES " + amount + " posted for " + s.getFirstName() + " " + s.getLastName() + ". Receipt: " + receipt);
        return "redirect:/admin/finance";
    }
}
