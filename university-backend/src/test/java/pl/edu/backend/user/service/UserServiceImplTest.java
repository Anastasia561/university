package pl.edu.backend.user.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.backend.auth.core.CustomUserDetails;
import pl.edu.backend.user.dto.PasswordDto;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.mapper.UserMapper;
import pl.edu.backend.user.model.Role;
import pl.edu.backend.user.model.User;
import pl.edu.backend.user.repository.UserRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldReturnUserById_whenUserExists() {
        User user = new User();

        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(user));

        User result = userService.findById(10);
        assertThat(result).isSameAs(user);

        verify(userRepository).findById(any(Integer.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserDoesNotExist() {
        when(userRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.findById(10)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findById(any(Integer.class));
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserDoesNotExistForProfile() {
        mockAuth();
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.getUserProfile()
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail(any(String.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldReturnUserProfile_whenUserExists() {
        mockAuth();
        User user = new User();
        UserProfileDto dto = new UserProfileDto("Test", "Test", "email");

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(userMapper.toUserProfileDto(user)).thenReturn(dto);

        UserProfileDto result = userService.getUserProfile();
        assertThat(result).isSameAs(dto);

        verify(userRepository).findByEmail(any(String.class));
        verify(userMapper).toUserProfileDto(user);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenUserDoesNotExistForChangePassword() {
        mockAuth();
        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());
        PasswordDto dto = new PasswordDto("pass", "pass");

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> userService.changePassword(dto)
        );

        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByEmail(any(String.class));
        verifyNoInteractions(userMapper);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPasswordsDoNotMatch() {
        mockAuth();

        PasswordDto dto = new PasswordDto("pass1", "pass");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.changePassword(dto)
        );

        assertEquals("Passwords do not match", exception.getMessage());

        verifyNoInteractions(userMapper, userRepository);
    }

    @Test
    void shouldChangePassword_whenPasswordsMatch() {
        mockAuth();
        PasswordDto dto = new PasswordDto("newPassword", "newPassword");

        User user = new User();
        user.setPassword("oldPassword");

        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        when(userRepository.findByEmail("student@example.com")).thenReturn(Optional.of(user));

        userService.changePassword(dto);

        assertThat(user.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).findByEmail("student@example.com");
        verify(passwordEncoder).encode("newPassword");
    }

    private void mockAuth() {
        CustomUserDetails userDetails = new CustomUserDetails(
                1,
                "student@example.com",
                "hashedPassword",
                Role.STUDENT
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
