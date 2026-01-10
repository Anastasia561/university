package pl.edu.backend.enrollment.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.student.model.Student;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EnrollmentMapperImplTest {
    private EnrollmentMapper enrollmentMapper;

    @BeforeEach
    void setUp() {
        enrollmentMapper = Mappers.getMapper(EnrollmentMapper.class);
    }

    @Test
    public void shouldMapEnrollmentCreateDtoToEntity_whenInputIsValid() {
        EnrollmentCreateDto dto = new EnrollmentCreateDto("code", "test@gmail.com",
                LocalDate.of(2030, 1, 1), 5.0);

        Enrollment enrollment = enrollmentMapper.toEnrollment(dto);
        assertEquals(LocalDate.of(2030, 1, 1), enrollment.getDate());
        assertEquals(5.0, enrollment.getFinalGrade());
    }

    @Test
    void shouldReturnNull_whenEnrollmentCreateDtoIsNull() {
        assertNull(enrollmentMapper.toEnrollment(null));
    }

    @Test
    public void shouldMapEnrollmentToPreviewDto_whenInputIsValid() {
        Course course = new Course();
        course.setCode("code");

        Student student = new Student();
        student.setEmail("test@gmail.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setFinalGrade(5.0);
        enrollment.setDate(LocalDate.of(2030, 1, 1));

        EnrollmentPreviewDto dto = enrollmentMapper.toEnrollmentPreviewDto(enrollment);

        assertEquals("code", dto.courseCode());
        assertEquals("test@gmail.com", dto.studentEmail());
        assertEquals(LocalDate.of(2030, 1, 1), dto.enrollmentDate());
        assertEquals(5.0, dto.finalGrade());
    }

    @Test
    void shouldReturnNull_whenEnrollmentForPreviewDtoIsNull() {
        assertNull(enrollmentMapper.toEnrollmentPreviewDto(null));
    }

    @Test
    public void shouldMapEnrollmentToViewDto_whenInputIsValid() {
        Course course = new Course();
        course.setCode("code");

        Student student = new Student();
        student.setEmail("test@gmail.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setFinalGrade(5.0);
        enrollment.setDate(LocalDate.of(2030, 1, 1));

        EnrollmentViewDto dto = enrollmentMapper.toEnrollmentViewDto(enrollment);

        assertEquals("code", dto.course().code());
        assertEquals("test@gmail.com", dto.student().email());
        assertEquals(LocalDate.of(2030, 1, 1), dto.enrollmentDate());
        assertEquals(5.0, dto.finalGrade());
    }

    @Test
    void shouldReturnNull_whenEnrollmentForViewDtoIsNull() {
        assertNull(enrollmentMapper.toEnrollmentViewDto(null));
    }

    @Test
    public void shouldUpdateEnrollmentFromDto_whenInputIsValid() {
        Course course = new Course();
        course.setCode("code");

        Student student = new Student();
        student.setEmail("test@gmail.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setFinalGrade(5.0);
        enrollment.setDate(LocalDate.of(2030, 1, 1));

        EnrollmentCreateDto dto = new EnrollmentCreateDto("code2", "test2@gmail.com",
                LocalDate.of(2040, 1, 1), 4.0);

        enrollmentMapper.updateFromDto(dto, enrollment);

        assertEquals("code", enrollment.getCourse().getCode());
        assertEquals("test@gmail.com", enrollment.getStudent().getEmail());
        assertEquals(LocalDate.of(2040, 1, 1), enrollment.getDate());
        assertEquals(4.0, enrollment.getFinalGrade());
    }

    @Test
    public void shouldNotUpdateEnrollmentFromDto_whenInputIsInvalid() {
        Course course = new Course();
        course.setCode("code");

        Student student = new Student();
        student.setEmail("test@gmail.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setFinalGrade(5.0);
        enrollment.setDate(LocalDate.of(2030, 1, 1));

        EnrollmentCreateDto dto = new EnrollmentCreateDto(null, null, null, null);

        enrollmentMapper.updateFromDto(dto, enrollment);

        assertEquals("code", enrollment.getCourse().getCode());
        assertEquals("test@gmail.com", enrollment.getStudent().getEmail());
        assertEquals(LocalDate.of(2030, 1, 1), enrollment.getDate());
        assertEquals(5.0, enrollment.getFinalGrade());
    }
}
