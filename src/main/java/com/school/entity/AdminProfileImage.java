package com.school.entity;

import jakarta.persistence.*;

@Entity
@Table(name="admin_profile_images")
public class AdminProfileImage extends BaseEntity {
    @OneToOne(optional=false)
    @JoinColumn(name="user_id", unique=true)
    private User user;

    @Lob
    @Column(nullable=false, columnDefinition="LONGBLOB")
    private byte[] data;

    @Column(nullable=false)
    private String contentType;

    protected AdminProfileImage() {}

    public AdminProfileImage(User user, byte[] data, String contentType) {
        this.user = user;
        this.data = data;
        this.contentType = contentType;
    }

    public User getUser() { return user; }
    public byte[] getData() { return data; }
    public String getContentType() { return contentType; }
    public void replace(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }
}
