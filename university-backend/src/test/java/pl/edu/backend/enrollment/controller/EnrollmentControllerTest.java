package pl.edu.backend.enrollment.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.backend.AbstractControllerIntegrationTest;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.user.model.Role;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnrollmentControllerTest extends AbstractControllerIntegrationTest {

    @Test
    void shouldReturnAllEnrollmentPreviewsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/enrollments?page={p}&size={s}", null, 0, 2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].courseCode").value("ALG"))
                .andExpect(jsonPath("$.content[0].studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.content[0].enrollmentDate").value("2024-02-10"))
                .andExpect(jsonPath("$.content[0].finalGrade").value(4.0))
                .andExpect(jsonPath("$.totalElements").value(5))
                .andExpect(jsonPath("$.totalPages").value(3))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldReturnAllEnrollmentPreviewsForStudent_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/enrollments?page={p}&size={s}", null, 0, 2)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].courseCode").value("ALG"))
                .andExpect(jsonPath("$.content[0].studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.content[0].enrollmentDate").value("2024-02-10"))
                .andExpect(jsonPath("$.content[0].finalGrade").value(4.0))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldReturnEnrollmentDetailsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID id = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/enrollments/details/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.name").value("Algorithms"))
                .andExpect(jsonPath("$.course.code").value("ALG"))
                .andExpect(jsonPath("$.student.email").value("anna.k@example.com"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andExpect(jsonPath("$.enrollmentDate").value("2024-02-10"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentDetailsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        UUID id = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/enrollments/details/{id}", null, id)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentDetailsNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("44444444-aaaa-bbbb-cccc-444444444444");

        performRequest(HttpMethod.GET, "/enrollments/details/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturnEnrollmentPreviewForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID id = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/enrollments/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("ALG"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andExpect(jsonPath("$.enrollmentDate").value("2024-02-10"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentPreviewAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        UUID id = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/enrollments/details/{id}", null, id)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentPreviewNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("44444444-aaaa-bbbb-cccc-444444444444");

        performRequest(HttpMethod.GET, "/enrollments/details/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturn404_whenEnrollmentNotFoundForUpdate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "jan.n@example.com",
                LocalDate.of(2030, 1, 1), 5.0);

        performRequest(HttpMethod.PUT, "/enrollments/{id}", dto, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentUpdateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");
        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "jan.n@example.com",
                LocalDate.of(2030, 1, 1), 5.0);

        performRequest(HttpMethod.PUT, "/enrollments/{id}", dto, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentUpdateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");
        EnrollmentCreateDto dto = new EnrollmentCreateDto(null, null,
                LocalDate.of(2010, 1, 1), -2.0);

        performRequest(HttpMethod.PUT, "/enrollments/{id}", dto, uuid)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.studentEmail").value("Student email is required"))
                .andExpect(jsonPath("$.fieldErrors.courseCode").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.enrollmentDate").value("Enrollment date must be in the future"))
                .andExpect(jsonPath("$.fieldErrors.finalGrade").value("Final grade can not be less than 2"));
    }

    @Test
    void shouldUpdateEnrollment_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID enrollmentId = createValidEnrollment();

        EnrollmentCreateDto dto = new EnrollmentCreateDto("NET", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        performRequest(HttpMethod.PUT, "/enrollments/{id}", dto, enrollmentId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("NET"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.enrollmentDate").value("2040-01-01"))
                .andExpect(jsonPath("$.finalGrade").value(4.0));
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentCreateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto(null, null,
                LocalDate.of(2010, 1, 1), -2.0);

        performRequest(HttpMethod.POST, "/enrollments", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.studentEmail").value("Student email is required"))
                .andExpect(jsonPath("$.fieldErrors.courseCode").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.enrollmentDate").value("Enrollment date must be in the future"))
                .andExpect(jsonPath("$.fieldErrors.finalGrade").value("Final grade can not be less than 2"));
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentCreateForAlreadyEnrolledStudent() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("OS", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        performRequest(HttpMethod.POST, "/enrollments", dto)
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Student with email anna.k@example.com is already enrolled for course with code OS"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentCreateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        performRequest(HttpMethod.POST, "/enrollments", dto)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldCreateEnrollment_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("NET", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        performRequest(HttpMethod.POST, "/enrollments", dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseCode").value("NET"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.enrollmentDate").value("2040-01-01"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void shouldDeleteEnrollment_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID courseId = createValidEnrollment();

        performRequest(HttpMethod.DELETE, "/enrollments/{id}", null, courseId)
                .andExpect(status().isNoContent());

        Long count = em.createQuery("SELECT COUNT(e) FROM Course e WHERE e.uuid = :uuid", Long.class)
                .setParameter("uuid", courseId)
                .getSingleResult();

        assertThat(count).isEqualTo(0L);
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentDeleteAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.DELETE, "/enrollments/{id}", null, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentNotFoundForDelete() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        performRequest(HttpMethod.DELETE, "/enrollments/{id}", null, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    private UUID createValidEnrollment() {
        Course course = em.createQuery("SELECT c FROM Course c WHERE c.code = :code", Course.class)
                .setParameter("code", "ALG")
                .getSingleResult();

        Student student = em.createQuery("SELECT u FROM Student u WHERE u.email = :email", Student.class)
                .setParameter("email", "anna.k@example.com")
                .getSingleResult();

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setDate(LocalDate.of(2030, 1, 1));
        enrollment.setFinalGrade(5.0);

        em.persist(enrollment);
        em.flush();
        em.clear();

        return enrollment.getUuid();
    }
}
