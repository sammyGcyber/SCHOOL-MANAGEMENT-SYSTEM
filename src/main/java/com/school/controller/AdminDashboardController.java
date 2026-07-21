package com.school.controller;

import com.school.entity.User;
import com.school.repository.*;
import java.math.BigDecimal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {
    private final StudentRepository students;
    private final LecturerRepository lecturers;
    private final UnitRepository units;
    private final PaymentRepository payments;
    private final AnnouncementRepository announcements;
    private final UserRepository users;
    private final AdminProfileImageRepository profileImages;

    public AdminDashboardController(StudentRepository students, LecturerRepository lecturers, UnitRepository units, PaymentRepository payments, AnnouncementRepository announcements, UserRepository users, AdminProfileImageRepository profileImages) {
        this.students = students;
        this.lecturers = lecturers;
        this.units = units;
        this.payments = payments;
        this.announcements = announcements;
        this.users = users;
        this.profileImages = profileImages;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication auth) {
        BigDecimal totalCollected = payments.findAll().stream()
                .map(p -> p.getAmount() != null ? p.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        User u = users.findByUsername(auth.getName()).orElse(null);

        model.addAttribute("active", "dashboard");
        boolean hasAdminImage = false;
        if (u != null) {
            try {
                hasAdminImage = profileImages.existsByUser(u);
            } catch (Exception ignored) {
                // An older local schema should not stop the administrator dashboard.
            }
        }
        model.addAttribute("hasAdminImage", hasAdminImage);
        model.addAttribute("studentCount", students.count());
        model.addAttribute("lecturerCount", lecturers.count());
        model.addAttribute("unitCount", units.count());
        model.addAttribute("totalCollected", totalCollected);
        model.addAttribute("announcementsCount", announcements.count());
        model.addAttribute("announcements", announcements.findAll());
        model.addAttribute("recentStudents", students.findAll().stream().limit(5).toList());
        return "admin/dashboard";
    }
}
