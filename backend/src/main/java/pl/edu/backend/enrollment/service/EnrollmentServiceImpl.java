package pl.edu.backend.enrollment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.repository.CourseRepository;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.mapper.EnrollmentMapper;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.enrollment.repository.EnrollmentRepository;
import pl.edu.backend.exception.StudentAlreadyEnrolledException;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.student.repository.StudentRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    @Override
    public List<EnrollmentPreviewDto> getAllPreview() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));

        List<Enrollment> enrollments;
        if (isAdmin) {
            enrollments = enrollmentRepository.findAll();
        } else {
            enrollments = enrollmentRepository.findByStudentEmail(email);
        }

        return enrollments.stream().map(enrollmentMapper::toEnrollmentPreviewDto).toList();
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
    public EnrollmentViewDto createEnrollment(EnrollmentCreateDto dto) {
        Enrollment saved = null;
        if (studentNotEnrolled(dto.studentEmail(), dto.courseCode())) {
            Student student = studentRepository.findByEmail(dto.studentEmail())
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
            Course course = courseRepository.findByCode(dto.courseCode())
                    .orElseThrow(() -> new IllegalArgumentException("Course not found"));

            Enrollment enrollment = enrollmentMapper.toEnrollment(dto);
            enrollment.setStudent(student);
            enrollment.setCourse(course);

            saved = enrollmentRepository.save(enrollment);
        }
        return enrollmentMapper.toEnrollmentViewDto(saved);
    }

    @Override
    @Transactional
    public EnrollmentViewDto updateEnrollment(UUID id, EnrollmentCreateDto dto) {
        Enrollment enrollment = enrollmentRepository.findByUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));

        boolean studentChanged = !enrollment.getStudent().getEmail().equals(dto.studentEmail());
        boolean courseChanged = !enrollment.getCourse().getCode().equals(dto.courseCode());

        if (studentChanged || courseChanged) {
            if (studentNotEnrolled(dto.studentEmail(), dto.courseCode())) {
                throw new IllegalArgumentException("Student already enrolled in this course");
            }

            Course course = courseRepository.findByCode(dto.courseCode())
                    .orElseThrow(() -> new EntityNotFoundException("Course not found"));
            Student student = studentRepository.findByEmail(dto.studentEmail())
                    .orElseThrow(() -> new EntityNotFoundException("Student not found"));

            enrollment.setCourse(course);
            enrollment.setStudent(student);
        }

        enrollmentMapper.updateFromDto(dto, enrollment);

        return enrollmentMapper.toEnrollmentViewDto(enrollment);
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
