package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.groups.GroupEntity;
import ua.edu.ukma.kataskin.smarthomeproject.dtos.api.device.DeviceType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class DeviceEntity {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DeviceType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomEntity room;

    @ManyToMany
    @JoinTable(
            name = "device_group_links",
            joinColumns = @JoinColumn(name = "device_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<GroupEntity> groups = new HashSet<>();

    public DeviceEntity() {
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public DeviceType getType() {
        return type;
    }

    public RoomEntity getRoom() {
        return room;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setType(DeviceType t) {
        this.type = t;
    }

    public void setRoom(RoomEntity r) {
        this.room = r;
    }

    public Set<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(Set<GroupEntity> groups) {
        this.groups = groups;
    }
}
