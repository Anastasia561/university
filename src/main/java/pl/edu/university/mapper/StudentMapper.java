package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.model.Student;
import pl.edu.university.model.dtos.enrollment.StudentForEnrollmentDto;

@Component
public class StudentMapper {
    public StudentForEnrollmentDto toStudentForEnrollmentDto(Student student) {
        return StudentForEnrollmentDto.builder()
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .build();
    }
}
