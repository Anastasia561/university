package pl.edu.backend.course.dto;

import java.time.LocalDate;

public record StudentForCourseDetailsDto(String email, Double finalGrade, LocalDate enrollmentDate) {
}
