package pl.edu.backend.enrollment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.edu.backend.enrollment.model.Enrollment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    @Query("""
            SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END
                        FROM Enrollment e
                        WHERE e.student.email = :email
                        AND e.course.code = :code
                        AND e.date > CURRENT_DATE
            """)
    boolean isStudentAlreadyEnrolled(@Param("email") String studentEmail,
                                     @Param("code") String courseCode);

    @Query("""
            SELECT e FROM Enrollment e
                        WHERE e.student.email = :email
                        AND e.course.code = :code
            """)
    List<Enrollment> findByStudentEmailAndCourseCode(@Param("email") String studentEmail,
                                                     @Param("code") String code);

    Page<Enrollment> findByStudentEmail(@Param("email") String email, Pageable pageable);

    Optional<Enrollment> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
