package pl.edu.university.model.dtos.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentForCourseDetailsDto {
    private String studentEmail;
    private Double finalGrade;
}
