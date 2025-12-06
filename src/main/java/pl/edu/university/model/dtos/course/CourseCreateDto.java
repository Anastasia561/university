package pl.edu.university.model.dtos.course;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.edu.university.validation.annotation.UniqueCode;

@Getter
@Setter
@Builder
public class CourseCreateDto {
    @NotBlank(message = "Course name is required")
    @Size(min = 3, max = 50, message = "Course name should contain between 3 and 50 symbols")
    private String courseName;

    @UniqueCode
    @NotBlank(message = "Course code is required")
    @Size(min = 2, max = 5, message = "Course name should contain between 2 and 5 symbols")
    private String courseCode;

    @Positive(message = "Credit must be a positive number")
    private Integer credit;

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 100, message = "Course name should contain between 2 and 100 symbols")
    private String description;
}
