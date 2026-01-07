package pl.edu.backend.course.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import pl.edu.backend.validation.annotation.UniqueCode;

public record CourseCreateDto(
        @NotBlank(message = "Course name is required")
        @Size(min = 3, max = 50, message = "Course name should contain between 3 and 50 symbols")
        String name,

        @UniqueCode
        @NotBlank(message = "Course code is required")
        @Size(min = 2, max = 5, message = "Course name should contain between 2 and 5 symbols")
        String code,

        @Positive(message = "Credit must be a positive number")
        Integer credit,

        @NotBlank(message = "Description is required")
        @Size(min = 2, max = 100, message = "Course name should contain between 2 and 100 symbols")
        String description
) {
}
