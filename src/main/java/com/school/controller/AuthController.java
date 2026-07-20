package com.school.controller;
import org.springframework.stereotype.Controller; import org.springframework.web.bind.annotation.GetMapping;
@Controller public class AuthController { @GetMapping("/login") String login(){return "login/index";} @GetMapping("/403") String denied(){return "error/403";} }
