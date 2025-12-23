package pl.edu.university.enrollment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.edu.university.exception.StudentAlreadyEnrolledException;
import pl.edu.university.enrollment.mapper.EnrollmentMapper;
import pl.edu.university.course.model.Course;
import pl.edu.university.enrollment.model.Enrollment;
import pl.edu.university.student.model.Student;
import pl.edu.university.enrollment.dto.EnrollmentCreateDto;
import pl.edu.university.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.university.enrollment.dto.EnrollmentViewDto;
import pl.edu.university.course.repository.CourseRepository;
import pl.edu.university.enrollment.repository.EnrollmentRepository;
import pl.edu.university.student.repository.StudentRepository;

import java.util.List;
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
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toEnrollmentPreviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public EnrollmentViewDto getEnrollmentDetails(Integer id) {
        return enrollmentRepository.findById(id)
                .map(enrollmentMapper::toEnrollmentViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
    }

    @Override
    public EnrollmentPreviewDto getEnrollmentPreview(Integer id) {
        return enrollmentRepository.findById(id)
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
    public EnrollmentViewDto updateEnrollment(Integer id, EnrollmentCreateDto dto) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Enrollment not found"));
        Course course = courseRepository.findByCode(dto.courseCode())
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        Student student = studentRepository.findByEmail(dto.studentEmail())
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        enrollmentMapper.updateFromDto(dto, enrollment);
        enrollment.setCourse(course);
        enrollment.setStudent(student);

        return enrollmentMapper.toEnrollmentViewDto(enrollment);
    }

    @Override
    public void deleteEnrollment(Integer id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Enrollment not found");
        }
        enrollmentRepository.deleteById(id);
    }

    private boolean studentNotEnrolled(String email, String code) {
        if (enrollmentRepository.isStudentAlreadyEnrolled(email, code))
            throw new StudentAlreadyEnrolledException(email, code);
        return true;
    }
}
