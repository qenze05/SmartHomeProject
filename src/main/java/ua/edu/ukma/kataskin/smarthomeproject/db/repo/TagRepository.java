package ua.edu.ukma.kataskin.smarthomeproject.db.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.edu.ukma.kataskin.smarthomeproject.db.entity.TagEntity;
import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
