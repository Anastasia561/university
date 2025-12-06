package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Student;

import java.util.Optional;


public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String code);
}
