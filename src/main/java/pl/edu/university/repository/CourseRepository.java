package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Course;

import java.util.Optional;


public interface CourseRepository extends JpaRepository<Course, Integer> {
    Optional<Course> findByCode(String code);

    boolean existsByCode(String code);
}
