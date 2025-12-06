package pl.edu.university.model.dtos.course;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CoursePreviewDto {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Integer credit;
}
