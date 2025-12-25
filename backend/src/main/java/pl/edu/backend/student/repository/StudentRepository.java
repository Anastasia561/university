package pl.edu.backend.student.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.backend.student.model.Student;

import java.util.Optional;
import java.util.UUID;


public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUuid(UUID uuid);

    Optional<Student> findByEmail(String code);

    void deleteByUuid(UUID uuid);

    boolean existsByUuid(UUID id);

    boolean existsByEmail(String code);
}
