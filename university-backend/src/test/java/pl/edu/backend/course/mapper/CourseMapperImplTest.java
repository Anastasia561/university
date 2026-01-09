package pl.edu.backend.course.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.student.model.Student;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CourseMapperImplTest {
    private CourseMapper courseMapper;

    @BeforeEach
    void setUp() {
        courseMapper = Mappers.getMapper(CourseMapper.class);
    }

    @Test
    public void shouldMapCourseToCoursePreviewDto_whenInputIsValid() {
        Course course = new Course();
        course.setUuid(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"));
        course.setName("Course Name");
        course.setCode("GRT");
        course.setCredit(3);

        CoursePreviewDto dto = courseMapper.toCoursePreviewDto(course);

        assertEquals(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), dto.id());
        assertEquals("Course Name", dto.name());
        assertEquals("GRT", dto.code());
        assertEquals(3, dto.credit());
    }

    @Test
    void shouldReturnNull_whenCourseForPreviewIsNull() {
        assertNull(courseMapper.toCoursePreviewDto(null));
    }

    @Test
    public void shouldMapCourseToCourseViewDto_whenInputIsValid() {
        Student student = new Student();
        student.setEmail("test@gmail.com");

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setDate(LocalDate.of(2023, 1, 1));

        Course course = new Course();
        course.setName("Course Name");
        course.setCode("GRT");
        course.setCredit(3);
        course.setDescription("Course Description");
        course.setEnrollments(List.of(enrollment));

        CourseViewDto dto = courseMapper.toCourseViewDto(course);

        assertEquals("Course Name", dto.name());
        assertEquals("GRT", dto.code());
        assertEquals("Course Description", dto.description());
        assertEquals(3, dto.credit());

        assertEquals(1, dto.students().size());
        assertEquals("test@gmail.com", dto.students().getFirst().email());
        assertEquals(LocalDate.of(2023, 1, 1), dto.students().getFirst().enrollmentDate());
    }

    @Test
    void shouldReturnNull_whenCourseForViewIsNull() {
        assertNull(courseMapper.toCourseViewDto(null));
    }

    @Test
    public void shouldMapCourseCreateDtoToEntity_whenInputIsValid() {
        CourseCreateDto dto = new CourseCreateDto("Name", "cfd", 3, "descr");

        Course course = courseMapper.toEntity(dto);

        assertEquals("Name", course.getName());
        assertEquals("cfd", course.getCode());
        assertEquals(3, course.getCredit());
        assertEquals("descr", course.getDescription());
        assertTrue(course.getEnrollments().isEmpty());
    }

    @Test
    void shouldReturnNull_whenCourseCreateDtoIsNull() {
        assertNull(courseMapper.toEntity(null));
    }

    @Test
    void shouldUpdateCourseFromDto_whenInputIsValid() {
        Course course = new Course();
        course.setName("Old Name");
        course.setCode("old code");
        course.setCredit(3);
        course.setDescription("Old Description");

        CourseUpdateDto dto = new CourseUpdateDto("New Name", "New code", 5, "new descr");

        courseMapper.updateFromDto(dto, course);

        assertEquals("New Name", course.getName());
        assertEquals("New code", course.getCode());
        assertEquals(5, course.getCredit());
        assertEquals("new descr", course.getDescription());
        assertTrue(course.getEnrollments().isEmpty());
    }

    @Test
    void shouldNotUpdateCourse_whenFieldsAreValid() {
        Course course = new Course();
        course.setName("Old Name");
        course.setCode("old code");
        course.setCredit(3);
        course.setDescription("Old Description");

        CourseUpdateDto dto = new CourseUpdateDto(null, null, null, null);

        courseMapper.updateFromDto(dto, course);

        assertEquals("Old Name", course.getName());
        assertEquals("old code", course.getCode());
        assertEquals(3, course.getCredit());
        assertEquals("Old Description", course.getDescription());
        assertTrue(course.getEnrollments().isEmpty());
    }
}
