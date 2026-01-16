package pl.edu.backend.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.backend.AbstractControllerIntegrationTest;
import pl.edu.backend.user.dto.PasswordDto;
import pl.edu.backend.user.model.Role;
import pl.edu.backend.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractControllerIntegrationTest {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldReturn403_whenRequestedUserProfileAsStudent() throws Exception {
        obtainRoleBasedToken(Role.STUDENT);

        performRequest(HttpMethod.GET, "/users/profile", null)
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value("Access denied"));
    }

    @Test
    void shouldReturnUserProfileForAdmin_whenInputIsValid() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        performRequest(HttpMethod.GET, "/users/profile", null)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Adam"))
                .andExpect(jsonPath("$.lastName").value("Pawlak"))
                .andExpect(jsonPath("$.email").value("adam.p@example.com"));
    }

    @Test
    void shouldReturn403_whenRequestedUserProfileWithoutLogin() throws Exception {
        performRequest(HttpMethod.GET, "/users/profile", null)
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturn400_whenRequestedPasswordChangeWithInvalidParams() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        PasswordDto dto = new PasswordDto("pass", "pass");

        performRequest(HttpMethod.POST, "/users/password", dto)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password " +
                        "must be valid (at least 8 characters, at least one digit, at least one special symbol)"))
                .andExpect(jsonPath("$.fieldErrors.repeatPassword").value("Password " +
                        "must be valid (at least 8 characters, at least one digit, at least one special symbol)"));
    }

    @Test
    void shouldUpdatePassword_whenRequestedPasswordChangeWithValidParams() throws Exception {
        obtainRoleBasedToken(Role.ADMIN);

        PasswordDto dto = new PasswordDto("ValidPass123!", "ValidPass123!");

        performRequest(HttpMethod.POST, "/users/password", dto)
                .andExpect(status().isOk());

        User user = em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", "adam.p@example.com").getSingleResult();

        assertThat(passwordEncoder.matches("ValidPass123!", user.getPassword())).isTrue();
    }
}
