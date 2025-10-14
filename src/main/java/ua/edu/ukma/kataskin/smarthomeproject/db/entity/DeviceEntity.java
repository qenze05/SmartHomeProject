package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity @Table(name = "devices")
public class DeviceEntity {
    @Id @GeneratedValue
    private UUID id;

    @Column(nullable=false, length=120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=40)
    private DeviceType type;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name="room_id")
    private RoomEntity room;

    public enum DeviceType { UNSPECIFIED, SECURITY, MEDIA, AIR_CONDITIONER, VACUUM_CLEANER, LIGHTS }

    protected DeviceEntity() {}
    public UUID getId() { return id; }
    public String getName() { return name; }
    public DeviceType getType() { return type; }
    public RoomEntity getRoom() { return room; }
    public void setName(String n){ this.name=n; }
    public void setType(DeviceType t){ this.type=t; }
    public void setRoom(RoomEntity r){ this.room=r; }
}
