package pl.edu.backend.auth.controller;

import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MvcResult;
import pl.edu.backend.AbstractControllerIntegrationTest;
import pl.edu.backend.auth.dto.AuthRequestDto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;


public class AuthControllerTest extends AbstractControllerIntegrationTest {
    @Test
    void shouldLoginSuccessfully_whenCredentialsAreValid() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("adam.p@example.com", "111");

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));
    }

    @Test
    void shouldReturn403_whenCredentialsDoNotExist() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("test@example.com", "111");

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn400_whenCredentialsAreNotValid() throws Exception {
        AuthRequestDto dto = new AuthRequestDto("test", null);

        performRequest(HttpMethod.POST, "/auth/login", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password is required"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email must be in valid format"));
    }

    @Test
    void shouldRefreshToken_whenRefreshCookieIsPresent() throws Exception {
        AuthRequestDto loginDto = new AuthRequestDto("adam.p@example.com", "111");

        Cookie refreshCookie = performRequest(HttpMethod.POST, "/auth/login", loginDto)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getCookie("refreshToken");

        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(post("/auth/refresh").cookie(refreshCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void shouldReturnUnauthorized_whenRefreshCookieIsMissing() throws Exception {
        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldLogoutSuccessfully_whenRefreshCookieIsPresent() throws Exception {
        AuthRequestDto loginDto = new AuthRequestDto("adam.p@example.com", "111");

        MvcResult loginResponse = performRequest(HttpMethod.POST, "/auth/login", loginDto)
                .andExpect(status().isOk())
                .andReturn();

        String accessToken = JsonPath.read(loginResponse.getResponse().getContentAsString(), "$.accessToken");
        Cookie refreshCookie = loginResponse.getResponse().getCookie("refreshToken");

        assertThat(refreshCookie).isNotNull();

        mockMvc.perform(post("/auth/logout")
                        .cookie(refreshCookie)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isNoContent());
    }
}
