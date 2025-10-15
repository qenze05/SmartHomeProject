package ua.edu.ukma.kataskin.smarthomeproject.db.entity.groups;

import jakarta.persistence.*;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.DeviceEntity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "device_groups")
public class GroupEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(length = 512)
    private String description;

    @ManyToMany(mappedBy = "groups")
    private Set<DeviceEntity> devices = new HashSet<>();

    public GroupEntity() {
    }

    public GroupEntity(UUID id, String name) {
        this.id = id;
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DeviceEntity> getDevices() {
        return devices;
    }

    public void setDevices(Set<DeviceEntity> devices) {
        this.devices = devices;
    }
}