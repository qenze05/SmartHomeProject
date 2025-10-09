package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;

@Entity @Table(name = "rooms")
public class RoomEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    protected RoomEntity() {}
    public RoomEntity(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}