package pl.edu.backend.student.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.backend.AbstractControllerIntegrationTest;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.user.model.Role;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StudentControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldReturnPageOfStudentPreviewsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/students?page=0&size=2", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].firstName").value("Agnieszka"))
                .andExpect(jsonPath("$.content[0].lastName").value("Wojcik"))
                .andExpect(jsonPath("$.content[0].email").value("agnieszka.w@example.com"))
                .andExpect(jsonPath("$.totalElements").value(9))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldReturnAllStudentPreviewsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/students/all", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(9))
                .andExpect(jsonPath("$[0].firstName").value("Anna"))
                .andExpect(jsonPath("$[0].lastName").value("Kowalska"))
                .andExpect(jsonPath("$[0].email").value("anna.k@example.com"));
    }

    @Test
    void shouldReturnStudentDetailsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        String id = "11111111-1111-1111-1111-111111111111";

        performRequest(HttpMethod.GET, "/students/details/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Kowalska"))
                .andExpect(jsonPath("$.email").value("anna.k@example.com"))
                .andExpect(jsonPath("$.birthdate").value("2001-05-12"))
                .andExpect(jsonPath("$.enrollments").isArray())
                .andExpect(jsonPath("$.enrollments.length()").value(2))
                .andExpect(jsonPath("$.enrollments[0].enrollmentDate").value("2024-02-10"))
                .andExpect(jsonPath("$.enrollments[0].finalGrade").value(4.0));
    }

    @Test
    void shouldReturnStudentPreviewForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        String id = "11111111-1111-1111-1111-111111111111";

        performRequest(HttpMethod.GET, "/students/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Kowalska"))
                .andExpect(jsonPath("$.email").value("anna.k@example.com"));
    }

    @Test
    void shouldReturn403_whenRequestedAllStudentPreviewsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/students/all", null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn403_whenRequestedPagedStudentPreviewsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/students?page=0&size=2", null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn403_whenRequestedStudentPreviewsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        String id = "11111111-1111-1111-1111-111111111111";

        performRequest(HttpMethod.GET, "/students/{id}", null, id)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn403_whenRequestedStudentDetailsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        String id = "11111111-1111-1111-1111-111111111111";

        performRequest(HttpMethod.GET, "/students/details/{id}", null, id)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenStudentPreviewNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        performRequest(HttpMethod.GET, "/students/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found"));
    }

    @Test
    void shouldReturn404_whenStudentDetailsNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        performRequest(HttpMethod.GET, "/students/details/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found"));
    }

    @Test
    void shouldReturnStudentProfileForLoggedInStudent_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/students/profile", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Anna"))
                .andExpect(jsonPath("$.lastName").value("Kowalska"))
                .andExpect(jsonPath("$.email").value("anna.k@example.com"))
                .andExpect(jsonPath("$.birthdate").value("2001-05-12"));
    }

    @Test
    void shouldReturn403_whenRequestedStudentProfileAsAdmin() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/students/profile", null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenStudentNotFoundForUpdate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID nonExistingId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        StudentUpdateDto dto = new StudentUpdateDto("fTest", "lTest", "test@gmail.com",
                LocalDate.of(1900, 1, 10));

        performRequest(HttpMethod.PUT, "/students/{id}", dto, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found"));
    }

    @Test
    void shouldReturn403_whenRequestedStudentUpdateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");
        StudentUpdateDto dto = new StudentUpdateDto("fTest", "lTest", "test@gmail.com",
                LocalDate.of(1900, 1, 10));

        performRequest(HttpMethod.PUT, "/students/{id}", dto, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedStudentUpdateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");

        StudentUpdateDto dto = new StudentUpdateDto(null, null, "e",
                LocalDate.of(2023, 1, 10));

        performRequest(HttpMethod.PUT, "/students/{id}", dto, uuid)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.lastName").value("Last name is required"))
                .andExpect(jsonPath("$.fieldErrors.birthdate").value("Student must be at least 18 years old"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email should be valid"));
    }

    @Test
    void shouldUpdateStudent_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID studentId = createValidStudent();

        StudentUpdateDto dto = new StudentUpdateDto("newFirst", "newLast", "new@gmail.com",
                LocalDate.of(1900, 2, 2));

        performRequest(HttpMethod.PUT, "/students/{id}", dto, studentId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("newFirst"))
                .andExpect(jsonPath("$.lastName").value("newLast"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.birthdate").value("1900-02-02"));
    }

    @Test
    void shouldReturn403_whenRequestedStudentCreateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        StudentCreateDto dto = new StudentCreateDto("fTest", "lTest", "test@gmail.com",
                LocalDate.of(1900, 1, 10));

        performRequest(HttpMethod.POST, "/students", dto)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedStudentCreateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        StudentCreateDto dto = new StudentCreateDto(null, null, "e",
                LocalDate.of(2023, 1, 10));

        performRequest(HttpMethod.POST, "/students", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.lastName").value("Last name is required"))
                .andExpect(jsonPath("$.fieldErrors.birthdate").value("Student must be at least 18 years old"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email should be valid"));
    }

    @Test
    void shouldCreateStudent_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        StudentCreateDto dto = new StudentCreateDto("newFirst", "newLast", "new@gmail.com",
                LocalDate.of(1900, 2, 2));

        performRequest(HttpMethod.POST, "/students", dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("newFirst"))
                .andExpect(jsonPath("$.lastName").value("newLast"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.birthdate").value("1900-02-02"));
    }

    @Test
    void shouldDeleteStudent_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID studentId = createValidStudent();

        performRequest(HttpMethod.DELETE, "/students/{id}", null, studentId)
                .andExpect(status().isNoContent());

        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Student c WHERE c.uuid = :uuid", Long.class)
                .setParameter("uuid", studentId)
                .getSingleResult();

        assertThat(count).isEqualTo(0L);
    }

    @Test
    void shouldReturn403_whenRequestedStudentDeleteAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        UUID uuid = UUID.fromString("11111111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.DELETE, "/students/{id}", null, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenStudentNotFoundForDelete() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID nonExistingId = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        performRequest(HttpMethod.DELETE, "/students/{id}", null, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Student not found"));
    }

    @Test
    void shouldRegisterStudent_whenInputIsValid() throws Exception {
        StudentRegisterDto dto = new StudentRegisterDto("newFirst", "newLast", "new@gmail.com",
                "passValid123!", "passValid123!", LocalDate.of(1900, 2, 2));

        performRequest(HttpMethod.POST, "/students/register", dto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("newFirst"))
                .andExpect(jsonPath("$.lastName").value("newLast"))
                .andExpect(jsonPath("$.email").value("new@gmail.com"))
                .andExpect(jsonPath("$.birthdate").value("1900-02-02"));
    }

    @Test
    void shouldReturn400_whenRequestedStudentRegisterWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        StudentCreateDto dto = new StudentCreateDto(null, null, "e",
                LocalDate.of(2023, 1, 10));

        performRequest(HttpMethod.POST, "/students/register", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.firstName").value("First name is required"))
                .andExpect(jsonPath("$.fieldErrors.lastName").value("Last name is required"))
                .andExpect(jsonPath("$.fieldErrors.birthdate").value("Student must be at least 18 years old"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email should be valid"));
    }

    private UUID createValidStudent() {
        Student student = new Student();
        student.setFirstName("First test");
        student.setLastName("Last test");
        student.setEmail("test@mail.com");
        student.setBirthdate(LocalDate.of(1900, 1, 10));
        student.setUuid(UUID.randomUUID());
        student.setPassword("password");
        student.setRole(Role.STUDENT);

        em.persist(student);
        em.flush();
        em.clear();

        return student.getUuid();
    }
}
