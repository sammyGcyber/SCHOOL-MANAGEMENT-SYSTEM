package com.school.controller;

import com.school.dto.StudentForm;
import com.school.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/students")
public class AdminStudentController {
    private final StudentService service;

    public AdminStudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String q, Model model) {
        model.addAttribute("active", "students");
        model.addAttribute("students", service.search(q));
        model.addAttribute("q", q);
        return "admin/students";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("active", "students");
        model.addAttribute("studentForm", new StudentForm());
        return "admin/student-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute StudentForm studentForm, BindingResult errors, Model model, RedirectAttributes flash) {
        if (errors.hasErrors()) {
            model.addAttribute("active", "students");
            return "admin/student-form";
        }
        service.create(studentForm);
        flash.addFlashAttribute("success", "Student registered successfully.");
        return "redirect:/admin/students";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        service.delete(id);
        flash.addFlashAttribute("success", "Student record removed.");
        return "redirect:/admin/students";
    }
}
