package pl.edu.backend.course.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import pl.edu.backend.AbstractControllerIntegrationTest;
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.user.model.Role;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CourseControllerTest extends AbstractControllerIntegrationTest {

    @Test
    void shouldReturnPageOfCoursePreviews_whenInputIsValid() throws Exception {
        performRequest(HttpMethod.GET, "/courses?page=0&size=2", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name").value("Algorithms"))
                .andExpect(jsonPath("$.content[0].code").value("ALG"))
                .andExpect(jsonPath("$.content[0].credit").value(5))
                .andExpect(jsonPath("$.totalElements").value(10))
                .andExpect(jsonPath("$.totalPages").value(5))
                .andExpect(jsonPath("$.number").value(0));
    }

    @Test
    void shouldReturnAllCoursePreviewsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/courses/all", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].name").value("Algorithms"))
                .andExpect(jsonPath("$[0].code").value("ALG"))
                .andExpect(jsonPath("$[0].credit").value(5));
    }

    @Test
    void shouldReturnCourseDetailsForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        String id = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

        performRequest(HttpMethod.GET, "/courses/details/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Algorithms"))
                .andExpect(jsonPath("$.code").value("ALG"))
                .andExpect(jsonPath("$.credit").value(5))
                .andExpect(jsonPath("$.description").value("Study of algorithms"))
                .andExpect(jsonPath("$.students").isArray())
                .andExpect(jsonPath("$.students.length()").value(1))
                .andExpect(jsonPath("$.students[0].email").value("anna.k@example.com"))
                .andExpect(jsonPath("$.students[0].finalGrade").value(4.0))
                .andExpect(jsonPath("$.students[0].enrollmentDate").value("2024-02-10"));
    }

    @Test
    void shouldReturnCoursePreviewForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        String id = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

        performRequest(HttpMethod.GET, "/courses/{id}", null, id)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Algorithms"))
                .andExpect(jsonPath("$.code").value("ALG"))
                .andExpect(jsonPath("$.credit").value(5));
    }

    @Test
    void shouldReturn403_whenRequestedAllCoursePreviewsAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/courses/all", null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn403_whenRequestedCoursePreviewAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        String id = "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa";

        performRequest(HttpMethod.GET, "/courses/{id}", null, id)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenCoursePreviewNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/courses/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void shouldReturn404_whenCourseDetailsNotFound() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        performRequest(HttpMethod.GET, "/courses/details/{id}", null, notExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void shouldReturn404_whenCourseNotFoundForUpdate() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");
        CourseUpdateDto dto = new CourseUpdateDto(
                "Nonexistent", "XXX01", 5, "Description");

        performRequest(HttpMethod.PUT, "/courses/{id}", dto, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void shouldReturn403_whenRequestedCourseUpdateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);
        UUID uuid = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        CourseUpdateDto dto = new CourseUpdateDto(
                "Nonexistent", "XXX01", 5, "Description");

        performRequest(HttpMethod.PUT, "/courses/{id}", dto, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedCourseUpdateWithNonUniqueCode() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID uuid = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        CourseUpdateDto dto = new CourseUpdateDto(
                "Test", "DB", 5, "Description");

        performRequest(HttpMethod.PUT, "/courses/{id}", dto, uuid)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Course code must be unique"));
    }

    @Test
    void shouldReturn400_whenRequestedCourseUpdateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);
        UUID uuid = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");
        CourseUpdateDto dto = new CourseUpdateDto(
                "g", null, -1, null);

        performRequest(HttpMethod.PUT, "/courses/{id}", dto, uuid)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.name").value("Course name should contain between 3 and 50 symbols"))
                .andExpect(jsonPath("$.fieldErrors.code").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.description").value("Description is required"))
                .andExpect(jsonPath("$.fieldErrors.credit").value("Credit must be a positive number"));
    }

    @Test
    void shouldUpdateCourse_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID courseId = createValidCourse();

        CourseUpdateDto updateDto = new CourseUpdateDto(
                "Updated name", "YYY", 4, "Updated description");

        performRequest(HttpMethod.PUT, "/courses/{id}", updateDto, courseId)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated name"))
                .andExpect(jsonPath("$.code").value("YYY"))
                .andExpect(jsonPath("$.credit").value(4));
    }

    @Test
    void shouldCreateCourse_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        CourseCreateDto createDto = new CourseCreateDto("Test name", "YYY", 4,
                "Test description");

        performRequest(HttpMethod.POST, "/courses", createDto)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.code").value("YYY"))
                .andExpect(jsonPath("$.credit").value(4))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void shouldReturn403_whenRequestedCourseCreateAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        CourseCreateDto createDto = new CourseCreateDto("Test name", "YYY", 4,
                "Test description");

        performRequest(HttpMethod.POST, "/courses", createDto)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedCourseCreateWithInvalidParameters() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        CourseCreateDto dto = new CourseCreateDto("g", null, -1, null);

        performRequest(HttpMethod.POST, "/courses", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.name").value("Course name should contain between 3 and 50 symbols"))
                .andExpect(jsonPath("$.fieldErrors.code").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.description").value("Description is required"))
                .andExpect(jsonPath("$.fieldErrors.credit").value("Credit must be a positive number"));
    }

    @Test
    void shouldReturn400_whenRequestedCourseCreateWithNonUniqueCode() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        CourseCreateDto dto = new CourseCreateDto("Test", "DB", 5, "Description");

        performRequest(HttpMethod.POST, "/courses", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"));
    }

    @Test
    void shouldDeleteCourse_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID courseId = createValidCourse();

        performRequest(HttpMethod.DELETE, "/courses/{id}", null, courseId)
                .andExpect(status().isNoContent());

        Long count = em.createQuery(
                        "SELECT COUNT(c) FROM Course c WHERE c.uuid = :uuid", Long.class)
                .setParameter("uuid", courseId)
                .getSingleResult();

        assertThat(count).isEqualTo(0L);
    }

    @Test
    void shouldReturn403_whenRequestedCourseDeleteAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        UUID uuid = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

        performRequest(HttpMethod.DELETE, "/courses/{id}", null, uuid)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenCourseNotFoundForDelete() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        performRequest(HttpMethod.DELETE, "/courses/{id}", null, nonExistingId)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    private UUID createValidCourse() {
        Course course = new Course();
        course.setName("Test course");
        course.setCode("TC101");
        course.setCredit(5);
        course.setDescription("Initial description");

        em.persist(course);
        em.flush();
        em.clear();

        return course.getUuid();
    }
}
