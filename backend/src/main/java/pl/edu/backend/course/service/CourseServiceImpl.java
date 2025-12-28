package pl.edu.backend.course.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;
import pl.edu.backend.course.dto.StudentForCourseDetailsDto;
import pl.edu.backend.course.mapper.CourseMapper;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.repository.CourseRepository;
import pl.edu.backend.enrollment.repository.EnrollmentRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final EnrollmentRepository enrollmentRepository;

    @Override
    public List<CoursePreviewDto> getAllPreview() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        boolean isStudent = auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_STUDENT"));

        List<Course> courses;
        if (isStudent) {
            courses = courseRepository.findByStudentEmail(email);
        } else {
            courses = courseRepository.findAll();
        }

        return courses.stream().map(courseMapper::toCoursePreviewDto).toList();
    }

    @Override
    public CourseViewDto getCourseDetails(UUID courseId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        Course course = courseRepository.findByUuid(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        boolean isStudent = auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_STUDENT"));

        CourseViewDto courseViewDto = courseMapper.toCourseViewDto(course);
        if (isStudent) {
            List<StudentForCourseDetailsDto> enrollmentsInfo =
                    enrollmentRepository.findByStudentEmailAndCourseCode(email, course.getCode()).stream()
                            .map(e ->
                                    new StudentForCourseDetailsDto(email, e.getFinalGrade(), e.getDate()))
                            .toList();
            courseViewDto = new CourseViewDto(course.getName(), course.getCode(), course.getCredit(),
                    course.getDescription(), enrollmentsInfo);
        }

        return courseViewDto;
    }

    @Override
    public CoursePreviewDto getCoursePreview(UUID courseId) {
        return courseRepository.findByUuid(courseId)
                .map(courseMapper::toCoursePreviewDto)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public CourseViewDto createCourse(CourseCreateDto dto) {
        Course course = courseMapper.toEntity(dto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseViewDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseViewDto updateCourse(UUID courseId, CourseUpdateDto dto) {
        Course course = courseRepository.findByUuid(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getCode().equals(dto.code()) && courseRepository.existsByCode(dto.code())) {
            throw new IllegalArgumentException("Course code must be unique");
        }
        courseMapper.updateFromDto(dto, course);
        return courseMapper.toCourseViewDto(course);
    }

    @Override
    @Transactional
    public void deleteCourse(UUID courseId) {
        if (!courseRepository.existsByUuid(courseId)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepository.deleteByUuid(courseId);
    }
}
