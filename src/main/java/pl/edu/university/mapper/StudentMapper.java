package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.model.dtos.student.EnrollmentForStudentDto;
import pl.edu.university.model.dtos.student.StudentCreateDto;
import pl.edu.university.model.dtos.student.StudentPreviewDto;
import pl.edu.university.model.dtos.student.StudentViewDto;
import pl.edu.university.model.entity.Enrollment;
import pl.edu.university.model.entity.Role;
import pl.edu.university.model.entity.Student;
import pl.edu.university.model.dtos.enrollment.StudentForEnrollmentDto;
import pl.edu.university.repository.EnrollmentRepository;

import java.util.List;

@Component
public class StudentMapper {
    private static final String START_PASSWORD = "111";

    public StudentMapper(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    private final EnrollmentRepository enrollmentRepository;

    public StudentForEnrollmentDto toStudentForEnrollmentDto(Student student) {
        return StudentForEnrollmentDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .build();
    }

    public StudentPreviewDto toStudentPreviewDto(Student student) {
        return StudentPreviewDto.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .build();
    }

    public StudentViewDto toStudentViewDto(Student student) {
        StudentViewDto s = StudentViewDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .birthDate(student.getBirthdate())
                .build();

        List<EnrollmentForStudentDto> enrollments = enrollmentRepository.findByStudent(student).stream()
                .map(e -> {
                    return EnrollmentForStudentDto.builder()
                            .courseCode(e.getCourse().getCode())
                            .enrollmentDate(e.getDate())
                            .finalGrade(e.getFinalGrade())
                            .build();
                }).toList();

        s.setEnrollments(enrollments);
        return s;
    }

    public Student toStudent(StudentCreateDto dto) {
        return Student.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .birthdate(dto.getBirthDate())
                .password(START_PASSWORD)
                .role(Role.STUDENT)
                .build();
    }

    private EnrollmentForStudentDto enrollmentForStudentDto(Enrollment enrollment) {
        return EnrollmentForStudentDto.builder()
                .courseCode(enrollment.getCourse().getCode())
                .enrollmentDate(enrollment.getDate())
                .finalGrade(enrollment.getFinalGrade())
                .build();
    }
}
