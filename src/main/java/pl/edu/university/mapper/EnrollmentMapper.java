package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.exception.CourseNotFoundException;
import pl.edu.university.exception.StudentNotFoundException;
import pl.edu.university.model.Course;
import pl.edu.university.model.Enrollment;
import pl.edu.university.model.Student;
import pl.edu.university.model.dtos.enrollment.EnrollmentCreateDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentPreviewDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentViewDto;
import pl.edu.university.repository.CourseRepository;
import pl.edu.university.repository.StudentRepository;

@Component
public class EnrollmentMapper {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final StudentMapper studentMapper;

    public EnrollmentMapper(StudentRepository studentRepository, CourseRepository courseRepository, CourseMapper courseMapper, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
        this.studentMapper = studentMapper;
    }

    public Enrollment toEnrollment(EnrollmentCreateDto dto) {
        Course course = courseRepository.findByCode(dto.getCourseCode())
                .orElseThrow(() -> new CourseNotFoundException(dto.getCourseCode()));
        Student student = studentRepository.findByEmail(dto.getStudentEmail())
                .orElseThrow(() -> new StudentNotFoundException(dto.getStudentEmail()));
        return Enrollment.builder()
                .course(course)
                .student(student)
                .date(dto.getEnrollmentDate())
                .finalGrade(dto.getFinalGrade())
                .build();
    }

    public EnrollmentPreviewDto toEnrollmentPreviewDto(Enrollment enrollment) {
        return EnrollmentPreviewDto.builder()
                .id(enrollment.getId())
                .courseCode(enrollment.getCourse().getCode())
                .studentEmail(enrollment.getStudent().getEmail())
                .enrollmentDate(enrollment.getDate())
                .finalGrade(enrollment.getFinalGrade())
                .build();
    }

    public EnrollmentViewDto toEnrollmentViewDto(Enrollment enrollment) {
        return EnrollmentViewDto.builder()
                .student(studentMapper.toStudentForEnrollmentDto(enrollment.getStudent()))
                .course(courseMapper.toCourseForEnrollmentDto(enrollment.getCourse()))
                .enrollmentDate(enrollment.getDate())
                .finalGrade(enrollment.getFinalGrade())
                .build();
    }
}
