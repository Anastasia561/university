package pl.edu.university.model.dtos.enrollment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Builder
public class EnrollmentViewDto {
    private StudentForEnrollmentDto student;
    private CourseForEnrollmentDto course;
    private LocalDate enrollmentDate;
    private Double finalGrade;
}
