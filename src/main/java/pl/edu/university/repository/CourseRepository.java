package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Course;


public interface CourseRepository extends JpaRepository<Course, Integer> {
    boolean existsByCode(String code);
}
