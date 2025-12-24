package pl.edu.backend.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.backend.student.model.Student;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String code);

    boolean existsByEmail(String code);
}
