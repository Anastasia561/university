package pl.edu.backend.course.service;

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
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;
import pl.edu.backend.course.mapper.CourseMapper;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.repository.CourseRepository;
import pl.edu.backend.user.model.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void shouldReturnAllCoursePreviewsPageable_whenRequestedByStudent() {
        mockAuth(Role.STUDENT);

        Course course1 = new Course();
        course1.setName("Math");
        Course course2 = new Course();
        course2.setName("Physics");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());
        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2), pageable, 2);

        when(courseRepository.findByStudentEmail(eq("student@example.com"), any(Pageable.class)))
                .thenReturn(coursePage);

        CoursePreviewDto dto1 = new CoursePreviewDto(UUID.randomUUID(), "Math", "m", 2);
        CoursePreviewDto dto2 = new CoursePreviewDto(UUID.randomUUID(), "Physics", "m", 3);

        when(courseMapper.toCoursePreviewDto(course1)).thenReturn(dto1);
        when(courseMapper.toCoursePreviewDto(course2)).thenReturn(dto2);

        Page<CoursePreviewDto> result = courseService.getAllPreviewPageable(0, 2);

        assertEquals(2, result.getContent().size());
        assertEquals("Math", result.getContent().get(0).name());
        assertEquals("Physics", result.getContent().get(1).name());

        verify(courseRepository).findByStudentEmail(eq("student@example.com"), any(Pageable.class));
        verify(courseMapper).toCoursePreviewDto(course1);
        verify(courseMapper).toCoursePreviewDto(course2);
    }

    @Test
    void shouldReturnAllCoursePreviewsPageable_whenRequestedByAdmin() {
        mockAuth(Role.ADMIN);

        Course course1 = new Course();
        course1.setName("Math");
        Course course2 = new Course();
        course2.setName("Physics");

        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());
        Page<Course> coursePage = new PageImpl<>(List.of(course1, course2), pageable, 2);

        when(courseRepository.findAll(any(Pageable.class))).thenReturn(coursePage);

        CoursePreviewDto dto1 = new CoursePreviewDto(UUID.randomUUID(), "Math", "m", 2);
        CoursePreviewDto dto2 = new CoursePreviewDto(UUID.randomUUID(), "Physics", "m", 3);

        when(courseMapper.toCoursePreviewDto(course1)).thenReturn(dto1);
        when(courseMapper.toCoursePreviewDto(course2)).thenReturn(dto2);

        Page<CoursePreviewDto> result = courseService.getAllPreviewPageable(0, 2);

        assertEquals(2, result.getContent().size());
        assertEquals("Math", result.getContent().get(0).name());
        assertEquals("Physics", result.getContent().get(1).name());

        verify(courseRepository).findAll(any(Pageable.class));
        verify(courseMapper).toCoursePreviewDto(course1);
        verify(courseMapper).toCoursePreviewDto(course2);
    }

    @Test
    void shouldReturnAllCoursePreviewsNonPageable_whenRequestedByAnyUser() {
        Course course1 = new Course();
        course1.setName("Math");
        Course course2 = new Course();
        course2.setName("Physics");

        when(courseRepository.findAll()).thenReturn(List.of(course1, course2));

        CoursePreviewDto dto1 = new CoursePreviewDto(UUID.randomUUID(), "Math", "m", 2);
        CoursePreviewDto dto2 = new CoursePreviewDto(UUID.randomUUID(), "Physics", "m", 3);

        when(courseMapper.toCoursePreviewDto(course1)).thenReturn(dto1);
        when(courseMapper.toCoursePreviewDto(course2)).thenReturn(dto2);

        List<CoursePreviewDto> result = courseService.getAllPreviewNonPageable();

        assertEquals(2, result.size());
        assertEquals("Math", result.get(0).name());
        assertEquals("Physics", result.get(1).name());

        verify(courseRepository).findAll();
        verify(courseMapper).toCoursePreviewDto(course1);
        verify(courseMapper).toCoursePreviewDto(course2);
    }

    @Test
    void shouldReturnCourseDetails_whenRequestedByAdmin() {
        mockAuth(Role.ADMIN);

        Course course = new Course();
        course.setName("Math");

        when(courseRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(course));

        CourseViewDto dto = new CourseViewDto("Test name", "alg", 3, "Test descr", new ArrayList<>());

        when(courseMapper.toCourseViewDto(course)).thenReturn(dto);

        CourseViewDto result = courseService.getCourseDetails(UUID.randomUUID());

        assertEquals("Test name", result.name());
        assertEquals("alg", result.code());
        assertEquals("Test descr", result.description());
        assertTrue(result.students().isEmpty());

        verify(courseRepository).findByUuid(any(UUID.class));
        verify(courseMapper).toCourseViewDto(course);
    }

    @Test
    void shouldReturnCourseDetails_whenRequestedByStudent() {
        mockAuth(Role.STUDENT);

        Course course = new Course();
        course.setName("Math");
        course.setCode("alg");
        course.setDescription("Test descr");

        when(courseRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(course));

        CourseViewDto dto = new CourseViewDto(course.getName(), "alg", 3, "Test descr", new ArrayList<>());

        when(courseMapper.toCourseViewDto(course)).thenReturn(dto);

        CourseViewDto result = courseService.getCourseDetails(UUID.randomUUID());

        assertEquals("Math", result.name());
        assertEquals("alg", result.code());
        assertEquals("Test descr", result.description());
        assertTrue(result.students().isEmpty());

        verify(courseRepository).findByUuid(any(UUID.class));
        verify(courseMapper).toCourseViewDto(course);
    }

    @Test
    void shouldReturnCoursePreview_whenIdParameterIsValid() {
        Course course = new Course();
        course.setName("Math");
        course.setCode("alg");
        course.setDescription("Test descr");

        when(courseRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(course));
        CoursePreviewDto dto = new CoursePreviewDto(UUID.randomUUID(), "Math", "alg", 5);

        when(courseMapper.toCoursePreviewDto(course)).thenReturn(dto);

        CoursePreviewDto result = courseService.getCoursePreview(UUID.randomUUID());

        assertEquals("Math", result.name());
        assertEquals("alg", result.code());

        verify(courseRepository).findByUuid(any(UUID.class));
        verify(courseMapper).toCoursePreviewDto(course);
    }

    @Test
    void shouldReturnCourseByCode_whenCodeParameterIsValid() {
        Course course = new Course();
        course.setName("Math");
        course.setCode("alg");
        course.setDescription("Test descr");

        when(courseRepository.findByCode(any(String.class))).thenReturn(Optional.of(course));

        Course result = courseService.getCourseByCode("alg");

        assertEquals("Math", result.getName());
        assertEquals("alg", result.getCode());
        assertEquals("Test descr", result.getDescription());

        verify(courseRepository).findByCode(any(String.class));
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenCodeParameterIsNotValid() {
        when(courseRepository.findByCode(any(String.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.getCourseByCode("gg")
        );

        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository).findByCode(any(String.class));
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenIdParameterIsNotValid() {
        when(courseRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.getCoursePreview(UUID.randomUUID())
        );

        assertEquals("Course not found", exception.getMessage());

        verify(courseRepository).findByUuid(any(UUID.class));
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldCreateCourseSuccessfully_whenInputIsValid() {
        CourseCreateDto createDto = new CourseCreateDto("Test name", "Math", 3, "Test descr");

        Course course = new Course();
        course.setName("Math");

        Course savedCourse = new Course();
        savedCourse.setId(1);
        savedCourse.setName("Math");

        CoursePreviewDto viewDto = new CoursePreviewDto(UUID.randomUUID(), "Test name", "Math", 5);

        when(courseMapper.toEntity(createDto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toCoursePreviewDto(savedCourse)).thenReturn(viewDto);

        CoursePreviewDto result = courseService.createCourse(createDto);

        assertEquals(viewDto, result);

        verify(courseMapper).toEntity(createDto);
        verify(courseRepository).save(course);
        verify(courseMapper).toCoursePreviewDto(savedCourse);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenCourseDoesNotExistForUpdate() {
        UUID courseId = UUID.randomUUID();
        CourseUpdateDto dto = new CourseUpdateDto("Math", "MATH101", 3, "Test descr");

        when(courseRepository.findByUuid(courseId)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.updateCourse(courseId, dto)
        );

        assertEquals("Course not found", ex.getMessage());

        verify(courseRepository).findByUuid(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenCourseCodeIsNotUnique() {
        UUID courseId = UUID.randomUUID();

        Course existingCourse = new Course();
        existingCourse.setCode("OLD_CODE");

        CourseUpdateDto dto = new CourseUpdateDto("Math", "NEW_CODE", 3, "test descr");

        when(courseRepository.findByUuid(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseRepository.existsByCode("NEW_CODE")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> courseService.updateCourse(courseId, dto)
        );

        assertEquals("Course code must be unique", ex.getMessage());

        verify(courseRepository).findByUuid(courseId);
        verify(courseRepository).existsByCode("NEW_CODE");
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldUpdateCourse_whenCodeIsUnchanged() {
        UUID courseId = UUID.randomUUID();

        Course course = new Course();
        course.setCode("MATH101");

        CourseUpdateDto dto = new CourseUpdateDto(
                "Math updated", "MATH101", 3, "test descr");

        CoursePreviewDto viewDto = new CoursePreviewDto(UUID.randomUUID(), "MATH101", "code", 3);

        when(courseRepository.findByUuid(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toCoursePreviewDto(course)).thenReturn(viewDto);

        CoursePreviewDto result = courseService.updateCourse(courseId, dto);

        assertEquals(viewDto, result);

        verify(courseRepository).findByUuid(courseId);
        verify(courseMapper).updateFromDto(dto, course);
        verify(courseMapper).toCoursePreviewDto(course);
        verify(courseRepository, never()).existsByCode(any());
    }

    @Test
    void shouldUpdateCourse_whenCodeIsChangedAndUnique() {
        UUID courseId = UUID.randomUUID();

        Course course = new Course();
        course.setCode("OLD_CODE");

        CourseUpdateDto dto = new CourseUpdateDto("Math updated", "NEW_CODE", 3, "test descr");

        CoursePreviewDto viewDto = new CoursePreviewDto(UUID.randomUUID(), "Math updated", "NEW_CODE", 3);

        when(courseRepository.findByUuid(courseId)).thenReturn(Optional.of(course));
        when(courseRepository.existsByCode("NEW_CODE")).thenReturn(false);
        when(courseMapper.toCoursePreviewDto(course)).thenReturn(viewDto);

        CoursePreviewDto result = courseService.updateCourse(courseId, dto);

        assertEquals(viewDto, result);

        verify(courseRepository).findByUuid(courseId);
        verify(courseRepository).existsByCode("NEW_CODE");
        verify(courseMapper).updateFromDto(dto, course);
        verify(courseMapper).toCoursePreviewDto(course);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenCourseDoNotExistForDelete() {
        UUID courseId = UUID.randomUUID();

        when(courseRepository.existsByUuid(courseId)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(
                EntityNotFoundException.class,
                () -> courseService.deleteCourse(courseId)
        );

        assertEquals("Course not found", ex.getMessage());

        verify(courseRepository).existsByUuid(courseId);
        verifyNoInteractions(courseMapper);
    }

    @Test
    void shouldDeleteCourse_whenCourseExists() {
        UUID courseId = UUID.randomUUID();

        when(courseRepository.existsByUuid(courseId)).thenReturn(true);

        courseService.deleteCourse(courseId);

        verify(courseRepository).existsByUuid(courseId);
        verify(courseRepository).deleteByUuid(courseId);
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
