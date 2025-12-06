package pl.edu.university.model.dtos.course;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
public class CourseViewDto {
    private Integer id;
    private String courseName;
    private String courseCode;
    private Integer credit;
    private String description;
    private List<StudentForCourseDetailsDto> students;
}
