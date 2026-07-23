package com.school.controller;

import com.school.entity.AdminProfileImage;
import com.school.entity.User;
import com.school.repository.AdminProfileImageRepository;
import com.school.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminProfileController {
    private final UserRepository users;
    private final AdminProfileImageRepository profileImages;
    private final PasswordEncoder passwordEncoder;

    public AdminProfileController(UserRepository users, AdminProfileImageRepository profileImages, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.profileImages = profileImages;
        this.passwordEncoder = passwordEncoder;
    }

    private User adminUser(Authentication auth) {
        if (auth == null || auth.getName() == null) {
            throw new org.springframework.security.authentication.InsufficientAuthenticationException("Not authenticated");
        }
        return users.findByUsername(auth.getName()).orElseThrow(() ->
            new org.springframework.security.core.userdetails.UsernameNotFoundException("Admin user not found: " + auth.getName())
        );
    }

    private boolean hasProfileImage(User user) {
        try {
            return profileImages.existsByUser(user);
        } catch (Exception ignored) {
            return false;
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication auth) {
        User u = adminUser(auth);
        model.addAttribute("active", "profile");
        model.addAttribute("adminUser", u);
        model.addAttribute("hasAdminImage", hasProfileImage(u));
        return "admin/profile";
    }

    @PostMapping("/profile")
    @Transactional
    public String profileSave(Authentication auth,
                              @RequestParam String fullName,
                              @RequestParam String email,
                              @RequestParam(required = false) String newPassword,
                              @RequestParam(required = false) MultipartFile profileImage) {
        User u = adminUser(auth);
        if (!u.getEmail().equalsIgnoreCase(email) && users.existsByEmail(email)) {
            return "redirect:/admin/profile?emailError";
        }
        u.setEmail(email);
        u.setFullName(fullName);
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            u.setPassword(passwordEncoder.encode(newPassword.trim()));
        }

        if (profileImage != null && !profileImage.isEmpty()) {
            String type = profileImage.getContentType();
            if (type == null || !type.startsWith("image/") || profileImage.getSize() > 2_000_000) {
                return "redirect:/admin/profile?imageError";
            }
            try {
                AdminProfileImage image = profileImages.findByUser(u)
                        .orElse(new AdminProfileImage(u, profileImage.getBytes(), type));
                image.replace(profileImage.getBytes(), type);
                profileImages.save(image);
            } catch (Exception e) {
                return "redirect:/admin/profile?imageError";
            }
        }
        users.save(u);
        return "redirect:/admin/profile?updated";
    }

    @GetMapping("/profile-picture")
    public ResponseEntity<byte[]> profilePicture(Authentication auth) {
        AdminProfileImage image;
        try {
            image = profileImages.findByUser(adminUser(auth)).orElse(null);
        } catch (Exception ignored) {
            image = null;
        }
        if (image == null) return ResponseEntity.notFound().build();
        MediaType type = image.getContentType() == null ? MediaType.IMAGE_JPEG : MediaType.parseMediaType(image.getContentType());
        return ResponseEntity.ok()
                .contentType(type)
                .cacheControl(CacheControl.noCache())
                .body(image.getData());
    }
}
