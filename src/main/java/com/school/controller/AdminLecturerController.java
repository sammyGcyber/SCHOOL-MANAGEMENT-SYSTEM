package com.school.controller;

import com.school.entity.Lecturer;
import com.school.entity.User;
import com.school.enums.RoleName;
import com.school.repository.LecturerRepository;
import com.school.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/lecturers")
public class AdminLecturerController {
    private final LecturerRepository lecturers;
    private final UserRepository users;
    private final PasswordEncoder passwordEncoder;

    public AdminLecturerController(LecturerRepository lecturers, UserRepository users, PasswordEncoder passwordEncoder) {
        this.lecturers = lecturers;
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        List<Lecturer> list = lecturers.findAll();
        if (q != null && !q.trim().isEmpty()) {
            String query = q.toLowerCase();
            list = list.stream()
                    .filter(l -> l.getFullName().toLowerCase().contains(query)
                            || l.getStaffNumber().toLowerCase().contains(query)
                            || l.getEmail().toLowerCase().contains(query))
                    .toList();
        }
        model.addAttribute("active", "lecturers");
        model.addAttribute("lecturers", list);
        model.addAttribute("q", q);
        return "admin/lecturers";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("active", "lecturers");
        return "admin/lecturer-form";
    }

    @PostMapping
    @Transactional
    public String create(@RequestParam String staffNumber, @RequestParam String fullName, @RequestParam String email, @RequestParam String phone, RedirectAttributes flash) {
        if (lecturers.findAll().stream().anyMatch(l -> l.getStaffNumber().equalsIgnoreCase(staffNumber) || l.getEmail().equalsIgnoreCase(email))) {
            flash.addFlashAttribute("error", "Lecturer with this staff number or email already exists.");
            return "redirect:/admin/lecturers/new";
        }

        String username = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        if (users.existsByUsername(username)) {
            username = staffNumber.toLowerCase().replace('/', '-');
        }

        User user = new User(username, email, passwordEncoder.encode("Lecturer@123"), fullName, RoleName.LECTURER);
        users.save(user);

        Lecturer lecturer = new Lecturer(staffNumber, fullName, email);
        lecturer.setUser(user);
        lecturer.setPhone(phone);
        lecturers.save(lecturer);

        flash.addFlashAttribute("success", "Lecturer registered successfully.");
        return "redirect:/admin/lecturers";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        lecturers.findById(id).ifPresent(l -> {
            User u = l.getUser();
            lecturers.delete(l);
            if (u != null) {
                users.delete(u);
            }
        });
        flash.addFlashAttribute("success", "Lecturer removed.");
        return "redirect:/admin/lecturers";
    }
}
