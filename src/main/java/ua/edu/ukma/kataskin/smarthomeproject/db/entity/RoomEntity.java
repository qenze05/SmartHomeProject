package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity @Table(name = "room")
public class RoomEntity {
    @Id @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DeviceEntity> devices = new ArrayList<>();

    public RoomEntity(String name) { this.name = name; }

    public RoomEntity() {

    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public List<DeviceEntity> getDevices() { return devices; }
    public void setName(String name) { this.name = name; }
}
