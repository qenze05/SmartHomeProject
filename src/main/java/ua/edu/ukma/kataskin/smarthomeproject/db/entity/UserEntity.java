package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;

@Entity @Table(name = "user")
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String name;


}
