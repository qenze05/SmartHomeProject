package ua.edu.ukma.kataskin.smarthomeproject.db.repository.schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.ScheduleEntity;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    List<ScheduleEntity> findByRoom_Id(Long roomId);
    List<ScheduleEntity> findByDevice_Id(UUID deviceId);
}
