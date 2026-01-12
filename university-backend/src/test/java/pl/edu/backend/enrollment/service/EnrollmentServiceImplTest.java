package pl.edu.backend.enrollment.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.edu.backend.auth.core.CustomUserDetails;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.service.CourseService;
import pl.edu.backend.enrollment.dto.CourseForEnrollmentDto;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.dto.StudentForEnrollmentDto;
import pl.edu.backend.enrollment.mapper.EnrollmentMapper;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.enrollment.repository.EnrollmentRepository;
import pl.edu.backend.exception.StudentAlreadyEnrolledException;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.student.service.StudentService;
import pl.edu.backend.user.model.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {
    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private EnrollmentMapper enrollmentMapper;

    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @Test
    public void shouldReturnAllEnrollmentsPreviewPageable_whenRequestedByAdmin() {
        mockAuth(Role.ADMIN);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("date").ascending());

        Enrollment enrollment1 = new Enrollment();
        Enrollment enrollment2 = new Enrollment();

        Page<Enrollment> mockPage = new PageImpl<>(List.of(enrollment1, enrollment2), pageable, 2);

        when(enrollmentRepository.findAll(pageable)).thenReturn(mockPage);

        EnrollmentPreviewDto dto1 = new EnrollmentPreviewDto(UUID.randomUUID(), "code1",
                "test1", LocalDate.of(2030, 2, 2), 5.0);
        EnrollmentPreviewDto dto2 = new EnrollmentPreviewDto(UUID.randomUUID(), "code2",
                "test2", LocalDate.of(2030, 2, 2), 5.0);

        when(enrollmentMapper.toEnrollmentPreviewDto(enrollment1)).thenReturn(dto1);
        when(enrollmentMapper.toEnrollmentPreviewDto(enrollment2)).thenReturn(dto2);

        Page<EnrollmentPreviewDto> result = enrollmentService.getAllPreview(0, 5);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
        assertTrue(result.getContent().contains(dto2));

        verify(enrollmentRepository).findAll(pageable);
        verify(enrollmentMapper).toEnrollmentPreviewDto(enrollment1);
        verify(enrollmentMapper).toEnrollmentPreviewDto(enrollment2);
    }

    @Test
    public void shouldReturnAllEnrollmentsPreviewPageable_whenRequestedByStudent() {
        mockAuth(Role.STUDENT);
        Pageable pageable = PageRequest.of(0, 5, Sort.by("date").ascending());

        Enrollment enrollment1 = new Enrollment();
        Enrollment enrollment2 = new Enrollment();

        Page<Enrollment> mockPage = new PageImpl<>(List.of(enrollment1, enrollment2), pageable, 2);

        when(enrollmentRepository.findByStudentEmail("student@example.com", pageable)).thenReturn(mockPage);

        EnrollmentPreviewDto dto1 = new EnrollmentPreviewDto(UUID.randomUUID(), "code1",
                "test1", LocalDate.of(2030, 2, 2), 5.0);
        EnrollmentPreviewDto dto2 = new EnrollmentPreviewDto(UUID.randomUUID(), "code2",
                "test2", LocalDate.of(2030, 2, 2), 5.0);

        when(enrollmentMapper.toEnrollmentPreviewDto(enrollment1)).thenReturn(dto1);
        when(enrollmentMapper.toEnrollmentPreviewDto(enrollment2)).thenReturn(dto2);

        Page<EnrollmentPreviewDto> result = enrollmentService.getAllPreview(0, 5);

        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().contains(dto1));
        assertTrue(result.getContent().contains(dto2));

        verify(enrollmentRepository).findByStudentEmail("student@example.com", pageable);
        verify(enrollmentMapper).toEnrollmentPreviewDto(enrollment1);
        verify(enrollmentMapper).toEnrollmentPreviewDto(enrollment2);
    }

    @Test
    public void shouldReturnEnrollmentDetails_whenInputIsValid() {
        UUID id = UUID.randomUUID();
        Enrollment enrollment = new Enrollment();
        StudentForEnrollmentDto student = new StudentForEnrollmentDto("test", "test", "email");
        CourseForEnrollmentDto course = new CourseForEnrollmentDto("code", "name", 3, "descr");
        EnrollmentViewDto dto = new EnrollmentViewDto(student, course,
                LocalDate.of(2030, 1, 1), 5.0);

        when(enrollmentRepository.findByUuid(id)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toEnrollmentViewDto(enrollment)).thenReturn(dto);

        EnrollmentViewDto result = enrollmentService.getEnrollmentDetails(id);

        assertNotNull(result);
        assertEquals(dto, result);

        verify(enrollmentRepository).findByUuid(id);
        verify(enrollmentMapper).toEnrollmentViewDto(enrollment);
    }

    @Test
    public void shouldThrowEntityNotFoundException_whenInputIsNotValid() {
        UUID id = UUID.randomUUID();
        when(enrollmentRepository.findByUuid(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.getEnrollmentDetails(id)
        );

        assertEquals("Enrollment not found", exception.getMessage());

        verify(enrollmentRepository).findByUuid(id);
        verifyNoInteractions(enrollmentMapper);
    }

    @Test
    public void shouldReturnEnrollmentPreview_whenInputIsValid() {
        UUID id = UUID.randomUUID();
        Enrollment enrollment = new Enrollment();

        EnrollmentPreviewDto dto = new EnrollmentPreviewDto(UUID.randomUUID(), "code2",
                "test2", LocalDate.of(2030, 2, 2), 5.0);

        when(enrollmentRepository.findByUuid(id)).thenReturn(Optional.of(enrollment));
        when(enrollmentMapper.toEnrollmentPreviewDto(enrollment)).thenReturn(dto);

        EnrollmentPreviewDto result = enrollmentService.getEnrollmentPreview(id);

        assertNotNull(result);
        assertEquals(dto, result);

        verify(enrollmentRepository).findByUuid(id);
        verify(enrollmentMapper).toEnrollmentPreviewDto(enrollment);
    }

    @Test
    public void shouldThrowEntityNotFoundException_whenInputIsNotValidForEnrollmentPreview() {
        UUID id = UUID.randomUUID();
        when(enrollmentRepository.findByUuid(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.getEnrollmentPreview(id)
        );

        assertEquals("Enrollment not found", exception.getMessage());

        verify(enrollmentRepository).findByUuid(id);
        verifyNoInteractions(enrollmentMapper);
    }

    @Test
    public void shouldCreateEnrollment_whenInputIsValidAndStudentIsNotAlreadyEnrolled() {
        String studentEmail = "student@example.com";
        String courseCode = "CS101";

        EnrollmentCreateDto dto = new EnrollmentCreateDto(courseCode, studentEmail,
                LocalDate.of(2030, 1, 1), 5.0);

        Student student = new Student();
        Course course = new Course();
        Enrollment enrollment = new Enrollment();
        Enrollment savedEnrollment = new Enrollment();
        EnrollmentPreviewDto previewDto = new EnrollmentPreviewDto(UUID.randomUUID(), "code2",
                "test2", LocalDate.of(2030, 2, 2), 5.0);

        when(enrollmentRepository.isStudentAlreadyEnrolled(studentEmail, courseCode)).thenReturn(false);

        when(studentService.getStudentByEmail(studentEmail)).thenReturn(student);
        when(courseService.getCourseByCode(courseCode)).thenReturn(course);
        when(enrollmentMapper.toEnrollment(dto)).thenReturn(enrollment);
        when(enrollmentRepository.save(enrollment)).thenReturn(savedEnrollment);
        when(enrollmentMapper.toEnrollmentPreviewDto(savedEnrollment)).thenReturn(previewDto);

        EnrollmentPreviewDto result = enrollmentService.createEnrollment(dto);

        assertEquals(previewDto, result);

        verify(studentService).getStudentByEmail(studentEmail);
        verify(courseService).getCourseByCode(courseCode);
        verify(enrollmentRepository).save(enrollment);
        verify(enrollmentMapper).toEnrollment(dto);
        verify(enrollmentMapper).toEnrollmentPreviewDto(savedEnrollment);
    }

    @Test
    void shouldNotCreateEnrollment_whenStudentAlreadyEnrolled() {
        String studentEmail = "student@example.com";
        String courseCode = "CS101";
        Student student = new Student();
        Course course = new Course();

        EnrollmentCreateDto dto = new EnrollmentCreateDto(courseCode, studentEmail,
                LocalDate.of(2030, 1, 1), 5.0);

        when(studentService.getStudentByEmail(studentEmail)).thenReturn(student);
        when(courseService.getCourseByCode(courseCode)).thenReturn(course);
        when(enrollmentRepository.isStudentAlreadyEnrolled(studentEmail, courseCode)).thenReturn(true);

        StudentAlreadyEnrolledException exception = assertThrows(
                StudentAlreadyEnrolledException.class,
                () -> enrollmentService.createEnrollment(dto)
        );

        assertEquals("Student with email student@example.com is already enrolled for course with code CS101",
                exception.getMessage());
        verifyNoInteractions(enrollmentMapper);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenEnrollmentWasNotFoundForUpdate() {
        String studentEmail = "student@example.com";
        String courseCode = "CS101";
        UUID id = UUID.randomUUID();

        EnrollmentCreateDto dto = new EnrollmentCreateDto(courseCode, studentEmail,
                LocalDate.of(2030, 1, 1), 5.0);

        when(enrollmentRepository.findByUuid(id)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.updateEnrollment(id, dto)
        );

        assertEquals("Enrollment not found", exception.getMessage());
        verifyNoInteractions(enrollmentMapper, studentService, courseService);
    }

    @Test
    public void shouldUpdateEnrollment_whenInputIsValid() {
        UUID enrollmentId = UUID.randomUUID();
        String oldStudentEmail = "old@example.com";
        String newStudentEmail = "new@example.com";
        String oldCourseCode = "CS101";
        String newCourseCode = "CS102";

        EnrollmentCreateDto dto = new EnrollmentCreateDto(
                newCourseCode, newStudentEmail, LocalDate.of(2030, 1, 1), 5.0);

        Student oldStudent = new Student();
        oldStudent.setEmail(oldStudentEmail);

        Course oldCourse = new Course();
        oldCourse.setCode(oldCourseCode);

        Enrollment existingEnrollment = new Enrollment();
        existingEnrollment.setStudent(oldStudent);
        existingEnrollment.setCourse(oldCourse);

        Student newStudent = new Student();
        newStudent.setEmail(newStudentEmail);

        Course newCourse = new Course();
        newCourse.setCode(newCourseCode);

        EnrollmentPreviewDto previewDto = new EnrollmentPreviewDto(UUID.randomUUID(), newCourseCode,
                newStudentEmail, LocalDate.of(2030, 1, 1), 5.0);

        when(enrollmentRepository.findByUuid(enrollmentId)).thenReturn(Optional.of(existingEnrollment));
        when(studentService.getStudentByEmail(newStudentEmail)).thenReturn(newStudent);
        when(courseService.getCourseByCode(newCourseCode)).thenReturn(newCourse);

        when(enrollmentRepository.isStudentAlreadyEnrolled(newStudentEmail, newCourseCode)).thenReturn(false);

        doNothing().when(enrollmentMapper).updateFromDto(dto, existingEnrollment);
        when(enrollmentMapper.toEnrollmentPreviewDto(existingEnrollment)).thenReturn(previewDto);

        EnrollmentPreviewDto result = enrollmentService.updateEnrollment(enrollmentId, dto);

        assertNotNull(result);
        assertEquals(previewDto, result);

        verify(enrollmentRepository).findByUuid(enrollmentId);
        verify(studentService).getStudentByEmail(newStudentEmail);
        verify(courseService).getCourseByCode(newCourseCode);
        verify(enrollmentMapper).updateFromDto(dto, existingEnrollment);
        verify(enrollmentMapper).toEnrollmentPreviewDto(existingEnrollment);
        assertEquals(newStudent, existingEnrollment.getStudent());
        assertEquals(newCourse, existingEnrollment.getCourse());
    }

    @Test
    public void shouldDeleteEnrollment_whenEnrollmentExists() {
        UUID enrollmentId = UUID.randomUUID();

        when(enrollmentRepository.existsByUuid(enrollmentId)).thenReturn(true);

        enrollmentService.deleteEnrollment(enrollmentId);
        verify(enrollmentRepository).existsByUuid(enrollmentId);
        verify(enrollmentRepository).deleteByUuid(enrollmentId);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenEnrollmentWasNotFoundForDelete() {
        UUID enrollmentId = UUID.randomUUID();

        when(enrollmentRepository.existsByUuid(enrollmentId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> enrollmentService.deleteEnrollment(enrollmentId)
        );

        assertEquals("Enrollment not found", exception.getMessage());
        verify(enrollmentRepository).existsByUuid(enrollmentId);
        verifyNoInteractions(enrollmentMapper, studentService, courseService);
    }

    private void mockAuth(Role role) {
        CustomUserDetails userDetails = new CustomUserDetails(
                1,
                "student@example.com",
                "hashedPassword",
                role
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
