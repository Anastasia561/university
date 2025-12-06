package pl.edu.university.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import pl.edu.university.exception.CourseNotFoundException;
import pl.edu.university.exception.EnrollmentNotFoundException;
import pl.edu.university.exception.StudentAlreadyEnrolledException;
import pl.edu.university.exception.StudentNotFoundException;
import pl.edu.university.mapper.EnrollmentMapper;
import pl.edu.university.model.Course;
import pl.edu.university.model.Enrollment;
import pl.edu.university.model.Student;
import pl.edu.university.model.dtos.enrollment.EnrollmentCreateDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentPreviewDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentViewDto;
import pl.edu.university.repository.CourseRepository;
import pl.edu.university.repository.EnrollmentRepository;
import pl.edu.university.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository, CourseRepository courseRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public List<EnrollmentPreviewDto> getAllPreview() {
        return enrollmentRepository.findAll().stream()
                .map(enrollmentMapper::toEnrollmentPreviewDto)
                .collect(Collectors.toList());
    }

    public EnrollmentViewDto getEnrollmentDetails(Integer id) {
        return enrollmentRepository.findById(id)
                .map(enrollmentMapper::toEnrollmentViewDto)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
    }

    public EnrollmentPreviewDto getEnrollmentPreview(Integer id) {
        return enrollmentRepository.findById(id)
                .map(enrollmentMapper::toEnrollmentPreviewDto)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
    }

    @Transactional
    public EnrollmentViewDto createEnrollment(EnrollmentCreateDto dto) {
        Enrollment saved = null;
        if (studentNotEnrolled(dto.getStudentEmail(), dto.getCourseCode())) {
            saved = enrollmentRepository.save(enrollmentMapper.toEnrollment(dto));
        }
        return enrollmentMapper.toEnrollmentViewDto(saved);
    }

    @Transactional
    public EnrollmentViewDto updateEnrollment(Integer id, EnrollmentCreateDto dto) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new EnrollmentNotFoundException(id));
        Course course = courseRepository.findByCode(dto.getCourseCode())
                .orElseThrow(() -> new CourseNotFoundException(dto.getCourseCode()));
        Student student = studentRepository.findByEmail(dto.getStudentEmail())
                .orElseThrow(() -> new StudentNotFoundException(dto.getStudentEmail()));

        enrollment.setCourse(course);
        enrollment.setStudent(student);
        enrollment.setDate(dto.getEnrollmentDate());
        enrollment.setFinalGrade(dto.getFinalGrade());

        return enrollmentMapper.toEnrollmentViewDto(enrollment);
    }

    @Transactional
    public void deleteEnrollment(Integer id) {
        if (!enrollmentRepository.existsById(id)) {
            throw new EnrollmentNotFoundException(id);
        }

        enrollmentRepository.deleteById(id);
    }

    private boolean studentNotEnrolled(String email, String code) {
        if (enrollmentRepository.isStudentAlreadyEnrolled(email, code))
            throw new StudentAlreadyEnrolledException(email, code);
        return true;
    }
}
