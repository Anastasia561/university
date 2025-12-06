package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.exception.CourseNotFoundException;
import pl.edu.university.exception.StudentNotFoundException;
import pl.edu.university.model.dtos.enrollment.CourseForEnrollmentDto;
import pl.edu.university.model.entity.Course;
import pl.edu.university.model.entity.Enrollment;
import pl.edu.university.model.entity.Student;
import pl.edu.university.model.dtos.enrollment.EnrollmentCreateDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentPreviewDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentViewDto;
import pl.edu.university.repository.CourseRepository;
import pl.edu.university.repository.StudentRepository;

@Component
public class EnrollmentMapper {
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final StudentMapper studentMapper;

    public EnrollmentMapper(StudentRepository studentRepository, CourseRepository courseRepository,
                            StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
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
                .course(toCourseForEnrollmentDto(enrollment.getCourse()))
                .enrollmentDate(enrollment.getDate())
                .finalGrade(enrollment.getFinalGrade())
                .build();
    }

    private CourseForEnrollmentDto toCourseForEnrollmentDto(Course c) {
        return CourseForEnrollmentDto.builder()
                .code(c.getCode())
                .name(c.getName())
                .credit(c.getCredit())
                .description(c.getDescription())
                .build();
    }
}
