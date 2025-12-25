package pl.edu.backend.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.backend.course.model.Course;

import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByUuid(UUID uuid);

    Optional<Course> findByCode(String code);

    void deleteByUuid(UUID id);

    boolean existsByUuid(UUID uuid);

    boolean existsByCode(String code);
}
