package pl.edu.university.model.dtos.enrollment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseForEnrollmentDto {
    private String code;
    private String name;
    private Integer credit;
    private String description;
}
