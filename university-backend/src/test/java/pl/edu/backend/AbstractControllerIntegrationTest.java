package pl.edu.backend;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import pl.edu.backend.auth.dto.AuthRequestDto;
import pl.edu.backend.auth.service.AuthService;
import pl.edu.backend.user.model.Role;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class AbstractControllerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    private AuthService authService;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected EntityManager em;

    @Autowired
    protected ObjectMapper objectMapper;

    private String token;

    protected void obtainRoleBasedToken(Role role) {
        String email = "adam.p@example.com";
        if (role.equals(Role.STUDENT))
            email = "anna.k@example.com";

        AuthRequestDto loginRequest = new AuthRequestDto(email, "111");
        token = authService.login(loginRequest).accessToken();
    }

    protected ResultActions performRequest(HttpMethod method, String url, Object body, Object... uriVars)
            throws Exception {
        MockHttpServletRequestBuilder requestBuilder;

        if (method == GET) {
            requestBuilder = get(url, uriVars);
        } else if (method == POST) {
            requestBuilder = post(url, uriVars);
        } else if (method == PUT) {
            requestBuilder = put(url, uriVars);
        } else if (method == DELETE) {
            requestBuilder = delete(url, uriVars);
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        if (token != null) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        if (body != null) {
            requestBuilder.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(body));
        }

        return mockMvc.perform(requestBuilder);
    }
}
