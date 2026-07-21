package com.school.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/admin")
    public String adminRoot() {
        return "redirect:/admin/dashboard";
    }
}
