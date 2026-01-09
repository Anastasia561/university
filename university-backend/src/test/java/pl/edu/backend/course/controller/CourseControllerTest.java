package pl.edu.backend.course.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.edu.backend.AbstractIntegrationTest;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.user.model.Role;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CourseControllerTest extends AbstractIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnPageOfCoursePreviews_whenInputIsValid() throws Exception {
        mockMvc.perform(get("/courses")
                        .param("page", "0")
                        .param("size", "2"))
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
        String token = obtainRoleBasedToken(Role.ADMIN);
        mockMvc.perform(get("/courses/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(10))
                .andExpect(jsonPath("$[0].name").value("Algorithms"))
                .andExpect(jsonPath("$[0].code").value("ALG"))
                .andExpect(jsonPath("$[0].credit").value(5));
    }

    @Test
    void shouldReturnCourseDetailsForAdmin_whenInputIsValid() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        mockMvc.perform(get("/courses/details/{id}", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .header("Authorization", "Bearer " + token))
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
        String token = obtainRoleBasedToken(Role.ADMIN);

        mockMvc.perform(get("/courses/{id}", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Algorithms"))
                .andExpect(jsonPath("$.code").value("ALG"))
                .andExpect(jsonPath("$.credit").value(5));

    }

        @Test
    void shouldReturn403_whenRequestedAllCoursePreviewsAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        mockMvc.perform(get("/courses/all")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn403_whenRequestedCoursePreviewAsStudent() throws Exception {
        String token = obtainRoleBasedToken(Role.STUDENT);

        mockMvc.perform(get("/courses/{id}", "aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturn404_whenCoursePreviewNotFound() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        mockMvc.perform(get("/courses/{id}", notExistingId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void shouldReturn404_whenCourseNotFound() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);
        UUID notExistingId = UUID.fromString("aaaa1111-1111-1111-1111-111111111111");

        mockMvc.perform(get("/courses/details/{id}", notExistingId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
    }

    @Test
    void shouldReturn404_whenCourseNotFoundForUpdate() throws Exception {
        String token = obtainRoleBasedToken(Role.ADMIN);

        UUID nonExistingId = UUID.fromString("99999999-9999-9999-9999-999999999999");
        CourseUpdateDto dto = new CourseUpdateDto(
                "Nonexistent", "XXX01", 5, "Description");

        String json = objectMapper.writeValueAsString(dto);

        mockMvc.perform(put("/courses/{id}", nonExistingId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found"));
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

}
