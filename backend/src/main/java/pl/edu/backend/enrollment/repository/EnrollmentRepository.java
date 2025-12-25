package pl.edu.backend.enrollment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.backend.enrollment.model.Enrollment;

import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM Enrollment e " +
            "WHERE e.student.email = :email " +
            "AND e.course.code = :code")
    boolean isStudentAlreadyEnrolled(@Param("email") String studentEmail,
                                     @Param("code") String courseCode);

    Optional<Enrollment> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
