package pl.edu.backend.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.backend.course.model.Course;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByUuid(UUID uuid);

    Optional<Course> findByCode(String code);

    @Query("""
            SELECT distinct c from Course c join c.enrollments e join e.student s where s.email = :email
            """)
    List<Course> findByStudentEmail(@Param("email") String email);

    void deleteByUuid(UUID id);

    boolean existsByUuid(UUID uuid);

    boolean existsByCode(String code);
}
