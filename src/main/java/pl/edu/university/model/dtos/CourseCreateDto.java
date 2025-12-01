package pl.edu.university.model.dtos;

import jakarta.validation.constraints.*;
import pl.edu.university.validation.annotation.UniqueCode;

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

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Integer getCredit() {
        return credit;
    }

    public void setCredit(Integer credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
