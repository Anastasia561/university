package pl.edu.university.model.dtos.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class StudentViewDto {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
    private List<EnrollmentForStudentDto> enrollments;
}
