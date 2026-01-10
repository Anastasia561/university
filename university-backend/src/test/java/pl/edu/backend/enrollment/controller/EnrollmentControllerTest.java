package pl.edu.backend.enrollment.controller;


import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.backend.AbstractIntegrationTest;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.user.model.Role;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EnrollmentControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnAllEnrollmentPreviewsForAdmin_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);
        mockMvc.perform(get("/enrollments")
                        .header("Authorization", "Bearer " + token).
                        param("page", "0")
                        .param("size", "2"))
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
        String token = obtainRoleBasedToken(Role.STUDENT);
        mockMvc.perform(get("/enrollments")
                        .header("Authorization", "Bearer " + token).
                        param("page", "0")
                        .param("size", "2"))
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
        String token = obtainRoleBasedToken(Role.ADMIN);

        mockMvc.perform(get("/enrollments/details/{id}", "aaaa1111-1111-1111-1111-111111111111")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.course.name").value("Algorithms"))
                .andExpect(jsonPath("$.course.code").value("ALG"))
                .andExpect(jsonPath("$.student.email").value("anna.k@example.com"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andExpect(jsonPath("$.enrollmentDate").value("2024-02-10"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentDetailsAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        mockMvc.perform(get("/enrollments/details/{id}", "aaaa1111-1111-1111-1111-111111111111")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentDetailsNotFound() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("44444444-aaaa-bbbb-cccc-444444444444");

        mockMvc.perform(get("/enrollments/details/{id}", notExistingId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturnEnrollmentPreviewForAdmin_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        mockMvc.perform(get("/enrollments/{id}", "aaaa1111-1111-1111-1111-111111111111")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("ALG"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andExpect(jsonPath("$.enrollmentDate").value("2024-02-10"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentPreviewAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        mockMvc.perform(get("/enrollments/{id}", "aaaa1111-1111-1111-1111-111111111111")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentPreviewNotFound() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("44444444-aaaa-bbbb-cccc-444444444444");

        mockMvc.perform(get("/enrollments/{id}", notExistingId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturn404_whenEnrollmentNotFoundForUpdate() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");
        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "jan.n@example.com",
                LocalDate.of(2030, 1, 1), 5.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/enrollments/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentUpdateAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");
        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "jan.n@example.com",
                LocalDate.of(2030, 1, 1), 5.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/enrollments/{id}", uuid)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentUpdateWithInvalidParameters() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");
        EnrollmentCreateDto dto = new EnrollmentCreateDto(null, null,
                LocalDate.of(2010, 1, 1), -2.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/enrollments/{id}", uuid)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.studentEmail").value("Student email is required"))
                .andExpect(jsonPath("$.fieldErrors.courseCode").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.enrollmentDate").value("Enrollment date must be in the future"))
                .andExpect(jsonPath("$.fieldErrors.finalGrade").value("Final grade can not be less than 2"));
    }

    @Test
    void shouldUpdateEnrollment_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID enrollmentId = createValidEnrollment(token);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("NET", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        String updateJson = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/enrollments/{id}", enrollmentId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courseCode").value("NET"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.enrollmentDate").value("2040-01-01"))
                .andExpect(jsonPath("$.finalGrade").value(4.0));

        deleteEnrollment(enrollmentId, token);
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentCreateWithInvalidParameters() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto(null, null,
                LocalDate.of(2010, 1, 1), -2.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/enrollments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.studentEmail").value("Student email is required"))
                .andExpect(jsonPath("$.fieldErrors.courseCode").value("Course code is required"))
                .andExpect(jsonPath("$.fieldErrors.enrollmentDate").value("Enrollment date must be in the future"))
                .andExpect(jsonPath("$.fieldErrors.finalGrade").value("Final grade can not be less than 2"));
    }

    @Test
    void shouldReturn400_whenRequestedEnrollmentCreateForAlreadyEnrolledStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("OS", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/enrollments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Student with email anna.k@example.com is already enrolled for course with code OS"));
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentCreateAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(post("/enrollments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldCreateEnrollment_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        EnrollmentCreateDto dto = new EnrollmentCreateDto("NET", "anna.k@example.com",
                LocalDate.of(2040, 1, 1), 4.0);

        String updateJson = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/enrollments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.courseCode").value("NET"))
                .andExpect(jsonPath("$.studentEmail").value("anna.k@example.com"))
                .andExpect(jsonPath("$.enrollmentDate").value("2040-01-01"))
                .andExpect(jsonPath("$.finalGrade").value(4.0))
                .andReturn().getResponse().getContentAsString();

        UUID enrollmentId = UUID.fromString(JsonPath.read(response, "$.id"));

        deleteEnrollment(enrollmentId, token);
    }

    @Test
    void shouldDeleteEnrollment_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID courseId = createValidEnrollment(token);

        mockMvc.perform(delete("/enrollments/{id}", courseId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/enrollments/{id}", courseId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn403_whenRequestedEnrollmentDeleteAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        UUID uuid = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        mockMvc.perform(delete("/enrollments/{id}", uuid)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenEnrollmentNotFoundForDelete() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");

        mockMvc.perform(delete("/enrollments/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Enrollment not found"));
    }

    private String obtainRoleBasedToken(Role role) throws Exception {
        String email = "adam.p@example.com";
        if (role.equals(Role.STUDENT))
            email = "anna.k@example.com";

        AuthRequestDto loginRequest = new AuthRequestDto(email, "111");
        String json = objectMapper.writeValueAsString(loginRequest);

        String response = mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(response, "$.accessToken");
    }

    private UUID createValidEnrollment(String token) throws Exception {
        EnrollmentCreateDto dto = new EnrollmentCreateDto("ALG", "anna.k@example.com",
                LocalDate.of(2030, 1, 1), 5.0);

        String createJson = objectMapper.writeValueAsString(dto);

        String response = mockMvc.perform(post("/enrollments")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return UUID.fromString(JsonPath.read(response, "$.id"));
    }

    private void deleteEnrollment(UUID enrollmentId, String token) throws Exception {
        mockMvc.perform(delete("/enrollments/{id}", enrollmentId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
