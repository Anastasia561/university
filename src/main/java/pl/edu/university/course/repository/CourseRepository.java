package pl.edu.university.course.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.course.model.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);
}
