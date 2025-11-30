package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Enrollment;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
}
