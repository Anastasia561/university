package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Course;
import pl.edu.university.model.Enrollment;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByCourse(Course c);
}
