package ua.edu.ukma.kataskin.smarthomeproject.db.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "tags")
public class TagEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true, length=50)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<DeviceEntity> devices = new HashSet<>();

    protected TagEntity() {}
    public TagEntity(String name){ this.name=name; }

    public Long getId(){ return id; }
    public String getName(){ return name; }
}
