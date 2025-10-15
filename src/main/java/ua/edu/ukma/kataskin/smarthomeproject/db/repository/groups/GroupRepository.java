package ua.edu.ukma.kataskin.smarthomeproject.db.repository.groups;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.groups.GroupEntity;

import java.util.UUID;

public interface GroupRepository extends JpaRepository<GroupEntity, UUID> {
}