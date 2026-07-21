package com.school.entity;

import com.school.enums.RoleName;
import jakarta.persistence.*;
import java.util.*;

@Entity @Table(name="users")
public class User extends BaseEntity {
 @Column(nullable=false, unique=true) private String username;
 @Column(nullable=false, unique=true) private String email;
 @Column(nullable=false) private String password;
 @Column(nullable=false) private String fullName;
 private boolean active=true;
 @ElementCollection(fetch=FetchType.EAGER) @CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user_id")) @Enumerated(EnumType.STRING) @Column(name="role")
 private Set<RoleName> roles=new HashSet<>();
 protected User(){}
 public User(String username,String email,String password,String fullName,RoleName role){this.username=username;this.email=email;this.password=password;this.fullName=fullName;this.roles.add(role);}
 public String getUsername(){return username;} public String getEmail(){return email;} public String getPassword(){return password;} public String getFullName(){return fullName;} public boolean isActive(){return active;} public Set<RoleName> getRoles(){return roles;}
 public void setPassword(String password){this.password=password;} public void setActive(boolean active){this.active=active;}
 public void setEmail(String email){this.email=email;} public void setFullName(String fullName){this.fullName=fullName;}
}
