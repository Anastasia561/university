package pl.edu.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.university.model.entity.Course;
import pl.edu.university.model.entity.Enrollment;
import pl.edu.university.model.entity.Student;

import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByCourse(Course c);

    List<Enrollment> findByStudent(Student s);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Enrollment e " +
            "WHERE e.student.email = :email " +
            "AND e.course.code = :code")
    boolean isStudentAlreadyEnrolled(@Param("email") String studentEmail,
                                     @Param("code") String courseCode);
}
