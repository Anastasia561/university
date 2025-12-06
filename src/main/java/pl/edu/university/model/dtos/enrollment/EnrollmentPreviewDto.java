package pl.edu.university.model.dtos.enrollment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class EnrollmentPreviewDto {
    private Integer id;
    private String courseCode;
    private String studentEmail;
    private LocalDate enrollmentDate;
    private Double finalGrade;
}
