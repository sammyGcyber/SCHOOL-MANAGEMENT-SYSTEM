package com.school.controller;

import com.school.entity.User;
import com.school.enums.RoleName;
import com.school.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
class AdminProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository users;

    @BeforeEach
    void seedAdmin() {
        if (users.findByUsername("admin").isEmpty()) {
            users.save(new User("admin", "admin@campus.edu", "hash", "System Administrator", RoleName.ADMIN));
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void profilePageLoads() throws Exception {
        mockMvc.perform(get("/admin/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("admin/profile"));
    }
}
