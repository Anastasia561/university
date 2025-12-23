package pl.edu.university.student.dto;

import java.time.LocalDate;

public record EnrollmentForStudentDto(
        String courseCode,
        LocalDate enrollmentDate,
        Double finalGrade
) {
}
