package com.school.controller;

import com.school.entity.*;
import com.school.repository.*;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/academic")
public class AdminAcademicController {
    private final UnitRepository units;
    private final LecturerRepository lecturers;
    private final FacultyRepository faculties;
    private final DepartmentRepository departments;
    private final ProgrammeRepository programmes;
    private final AcademicYearRepository years;
    private final SemesterRepository semesters;

    public AdminAcademicController(UnitRepository units, LecturerRepository lecturers, FacultyRepository faculties, DepartmentRepository departments, ProgrammeRepository programmes, AcademicYearRepository years, SemesterRepository semesters) {
        this.units = units;
        this.lecturers = lecturers;
        this.faculties = faculties;
        this.departments = departments;
        this.programmes = programmes;
        this.years = years;
        this.semesters = semesters;
    }

    @GetMapping
    public String academicView(Model model) {
        model.addAttribute("active", "academic");
        model.addAttribute("units", units.findAll());
        model.addAttribute("lecturers", lecturers.findAll());
        model.addAttribute("faculties", faculties.findAll());
        model.addAttribute("departments", departments.findAll());
        model.addAttribute("programmes", programmes.findAll());
        model.addAttribute("years", years.findAll());
        model.addAttribute("semesters", semesters.findAll());
        return "admin/academic";
    }

    @GetMapping("/units/new")
    public String unitForm(Model model) {
        model.addAttribute("active", "academic");
        model.addAttribute("lecturers", lecturers.findAll());
        return "admin/unit-form";
    }

    @PostMapping("/units")
    @Transactional
    public String createUnit(@RequestParam String code, @RequestParam String name, @RequestParam(defaultValue = "3") int creditHours, @RequestParam(required = false) Long lecturerId, RedirectAttributes flash) {
        if (units.findAll().stream().anyMatch(u -> u.getCode().equalsIgnoreCase(code))) {
            flash.addFlashAttribute("error", "Unit with code " + code + " already exists.");
            return "redirect:/admin/academic/units/new";
        }

        Unit unit = new Unit(code, name);
        if (lecturerId != null) {
            lecturers.findById(lecturerId).ifPresent(unit::setLecturer);
        }
        units.save(unit);
        flash.addFlashAttribute("success", "Unit " + code + " added successfully.");
        return "redirect:/admin/academic";
    }

    @PostMapping("/units/{id}/assign")
    @Transactional
    public String assignLecturer(@PathVariable Long id, @RequestParam(required = false) Long lecturerId, RedirectAttributes flash) {
        Unit unit = units.findById(id).orElseThrow();
        if (lecturerId != null && lecturerId > 0) {
            Lecturer l = lecturers.findById(lecturerId).orElse(null);
            unit.setLecturer(l);
        } else {
            unit.setLecturer(null);
        }
        units.save(unit);
        flash.addFlashAttribute("success", "Unit lecturer updated.");
        return "redirect:/admin/academic";
    }
}
