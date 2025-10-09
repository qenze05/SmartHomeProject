package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "rooms")
public class RoomEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceEntity> devices = new ArrayList<>();

    protected RoomEntity() {}
    public RoomEntity(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<DeviceEntity> getDevices() { return devices; }
    public void setName(String name) { this.name = name; }
}
