package pl.edu.backend.user.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.model.Role;
import pl.edu.backend.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class UserMapperImplTest {
    private UserMapper userMapper;

    @BeforeEach
    public void setup() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    public void shouldMapUserToUserProfileDto_whenInputIsValid() {
        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("Email");
        user.setPassword("Password");
        user.setRole(Role.STUDENT);

        UserProfileDto result = userMapper.toUserProfileDto(user);

        assertEquals("FirstName", result.firstName());
        assertEquals("LastName", result.lastName());
        assertEquals("Email", result.email());
    }

    @Test
    void shouldReturnNull_whenUserIsNull() {
        assertNull(userMapper.toUserProfileDto(null));
    }
}
