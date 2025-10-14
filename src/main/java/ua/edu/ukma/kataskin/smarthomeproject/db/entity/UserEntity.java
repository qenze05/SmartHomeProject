package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity @Table(name = "user")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(nullable=false, unique=true, length=50)
    private String username;

    @Column(nullable = false, length = 120)
    private String passwordHash;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }


}
