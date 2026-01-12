package pl.edu.backend.enrollment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.service.CourseService;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.mapper.EnrollmentMapper;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.enrollment.repository.EnrollmentRepository;
import pl.edu.backend.exception.StudentAlreadyEnrolledException;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.student.service.StudentService;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public Page<EnrollmentPreviewDto> getAllPreview(int page, int size) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        Page<Enrollment> enrollments;
        Pageable pageRequest = PageRequest.of(page, size, Sort.by("date").ascending());
        if (isAdmin) {
            enrollments = enrollmentRepository.findAll(pageRequest);
        } else {
            enrollments = enrollmentRepository.findByStudentEmail(email, pageRequest);
        }

        return enrollments.map(enrollmentMapper::toEnrollmentPreviewDto);
    }

    @Override
    public EnrollmentViewDto getEnrollmentDetails(UUID id) {
        return enrollmentRepository.findByUuid(id)
                .map(enrollmentMapper::toEnrollmentViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    @Override
    public EnrollmentPreviewDto getEnrollmentPreview(UUID id) {
        return enrollmentRepository.findByUuid(id)
                .map(enrollmentMapper::toEnrollmentPreviewDto)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    @Override
    @Transactional
    public EnrollmentPreviewDto createEnrollment(EnrollmentCreateDto dto) {
        Enrollment saved = null;

        Student student = studentService.getStudentByEmail(dto.studentEmail());
        Course course = courseService.getCourseByCode(dto.courseCode());

        if (studentNotEnrolled(dto.studentEmail(), dto.courseCode())) {
            Enrollment enrollment = enrollmentMapper.toEnrollment(dto);
            enrollment.setStudent(student);
            enrollment.setCourse(course);

            saved = enrollmentRepository.save(enrollment);
        }
        return enrollmentMapper.toEnrollmentPreviewDto(saved);
    }

    @Override
    @Transactional
    public EnrollmentPreviewDto updateEnrollment(UUID id, EnrollmentCreateDto dto) {
        Enrollment enrollment = enrollmentRepository.findByUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        boolean studentChanged = !enrollment.getStudent().getEmail().equals(dto.studentEmail());
        boolean courseChanged = !enrollment.getCourse().getCode().equals(dto.courseCode());

        Course course = courseService.getCourseByCode(dto.courseCode());
        Student student = studentService.getStudentByEmail(dto.studentEmail());

        if ((studentChanged || courseChanged) && studentNotEnrolled(dto.studentEmail(), dto.courseCode())) {
            enrollment.setCourse(course);
            enrollment.setStudent(student);
        }

        enrollmentMapper.updateFromDto(dto, enrollment);

        return enrollmentMapper.toEnrollmentPreviewDto(enrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(UUID id) {
        if (!enrollmentRepository.existsByUuid(id)) {
            throw new EntityNotFoundException("Enrollment not found");
        }
        enrollmentRepository.deleteByUuid(id);
    }

    private boolean studentNotEnrolled(String email, String code) {
        if (enrollmentRepository.isStudentAlreadyEnrolled(email, code))
            throw new StudentAlreadyEnrolledException(email, code);
        return true;
    }
}
