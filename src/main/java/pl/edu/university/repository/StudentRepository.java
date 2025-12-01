package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.edu.university.model.Student;


public interface StudentRepository extends JpaRepository<Student, Integer> {
}
