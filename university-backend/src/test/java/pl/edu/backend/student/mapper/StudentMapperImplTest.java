package pl.edu.backend.student.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.user.model.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentMapperImplTest {
    private StudentMapper studentMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        studentMapper = Mappers.getMapper(StudentMapper.class);
    }

    @Test
    void shouldMapStudentToStudentPreviewDto_whenInputIsValid() {
        Student student = new Student();
        student.setUuid(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email");

        StudentPreviewDto dto = studentMapper.toStudentPreviewDto(student);

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), dto.id());
        assertEquals("FirstName", dto.firstName());
        assertEquals("LastName", dto.lastName());
        assertEquals("Email", dto.email());
    }

    @Test
    void shouldReturnNull_whenStudentForPreviewIsNull() {
        assertNull(studentMapper.toStudentPreviewDto(null));
    }

    @Test
    void shouldMapStudentToStudentViewDto_whenInputIsValid() {
        Course course = new Course();
        course.setCode("GRT");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setDate(LocalDate.now());

        Student student = new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email");
        student.setBirthdate(LocalDate.of(1990, 1, 1));
        student.setEnrollments(List.of(enrollment));

        StudentViewDto dto = studentMapper.toStudentViewDto(student);

        assertEquals("FirstName", dto.firstName());
        assertEquals("LastName", dto.lastName());
        assertEquals("Email", dto.email());
        assertEquals(LocalDate.of(1990, 1, 1), dto.birthdate());
        assertEquals(1, dto.enrollments().size());
    }

    @Test
    void shouldReturnNull_whenStudentForViewIsNull() {
        assertNull(studentMapper.toStudentViewDto(null));
    }

    @Test
    void shouldMapStudentToStudentProfileDto_whenInputIsValid() {
        Student student = new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email");
        student.setBirthdate(LocalDate.of(1990, 1, 1));

        StudentProfileDto dto = studentMapper.toStudentProfileDto(student);

        assertEquals("FirstName", dto.firstName());
        assertEquals("LastName", dto.lastName());
        assertEquals("Email", dto.email());
        assertEquals(LocalDate.of(1990, 1, 1), dto.birthdate());
    }

    @Test
    void shouldReturnNull_whenProfileForViewIsNull() {
        assertNull(studentMapper.toStudentProfileDto(null));
    }

    @Test
    void shouldMapToStudent_whenInputIsValid() {
        StudentCreateDto studentCreateDto = new StudentCreateDto("First", "Last", "email",
                LocalDate.of(1990, 1, 1));

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(inv -> "ENC(" + inv.getArgument(0) + ")");
        Student result = studentMapper.toStudent(studentCreateDto, passwordEncoder);

        assertEquals("First", result.getFirstName());
        assertEquals("Last", result.getLastName());
        assertEquals("email", result.getEmail());
        assertEquals(Role.STUDENT, result.getRole());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertNotNull(result.getPassword());
    }

    @Test
    void shouldMapToStudentForRegistration_whenInputIsValid() {
        StudentRegisterDto dto = new StudentRegisterDto("First", "Last", "email",
                "pass", "pass", LocalDate.of(1990, 1, 1));

        when(passwordEncoder.encode(anyString()))
                .thenAnswer(inv -> "ENC(" + inv.getArgument(0) + ")");
        Student result = studentMapper.toStudentForRegistration(dto, passwordEncoder);

        assertEquals("First", result.getFirstName());
        assertEquals("Last", result.getLastName());
        assertEquals("email", result.getEmail());
        assertEquals(Role.STUDENT, result.getRole());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthdate());
        assertNotNull(result.getPassword());
    }

    @Test
    void shouldUpdateFromDto_whenInputIsValid() {
        Student student = new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email");
        student.setBirthdate(LocalDate.of(1990, 1, 1));

        StudentUpdateDto dto = new StudentUpdateDto("newFirst", "newLast",
                "newEmail", LocalDate.of(1990, 2, 2));

        studentMapper.updateFromDto(dto, student);

        assertEquals("newFirst", student.getFirstName());
        assertEquals("newLast", student.getLastName());
        assertEquals("newEmail", student.getEmail());
        assertEquals(LocalDate.of(1990, 2, 2), student.getBirthdate());
    }

    @Test
    void shouldNotUpdateFromDto_whenInputIsNotValid() {
        Student student = new Student();
        student.setFirstName("FirstName");
        student.setLastName("LastName");
        student.setEmail("Email");
        student.setBirthdate(LocalDate.of(1990, 1, 1));

        StudentUpdateDto dto = new StudentUpdateDto(null, null, null, null);

        studentMapper.updateFromDto(dto, student);

        assertEquals("FirstName", student.getFirstName());
        assertEquals("LastName", student.getLastName());
        assertEquals("Email", student.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), student.getBirthdate());
    }

}
