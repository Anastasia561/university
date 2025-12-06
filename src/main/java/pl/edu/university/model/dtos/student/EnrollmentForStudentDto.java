package pl.edu.university.model.dtos.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EnrollmentForStudentDto {
    private String courseCode;
    private LocalDate enrollmentDate;
    private Double finalGrade;
}
