package com.school.controller;

import com.school.entity.Announcement;
import com.school.repository.AnnouncementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/announcements")
public class AdminAnnouncementController {
    private final AnnouncementRepository announcements;

    public AdminAnnouncementController(AnnouncementRepository announcements) {
        this.announcements = announcements;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("active", "announcements");
        model.addAttribute("announcements", announcements.findAll());
        return "admin/announcements";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("active", "announcements");
        return "admin/announcement-form";
    }

    @PostMapping
    @Transactional
    public String create(@RequestParam String title, @RequestParam String content, RedirectAttributes flash) {
        Announcement announcement = new Announcement(title, content);
        announcements.save(announcement);
        flash.addFlashAttribute("success", "Announcement published successfully.");
        return "redirect:/admin/announcements";
    }

    @PostMapping("/{id}/delete")
    @Transactional
    public String delete(@PathVariable Long id, RedirectAttributes flash) {
        announcements.deleteById(id);
        flash.addFlashAttribute("success", "Announcement deleted.");
        return "redirect:/admin/announcements";
    }
}
