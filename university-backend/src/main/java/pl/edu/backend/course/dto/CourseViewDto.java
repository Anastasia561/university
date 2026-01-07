package pl.edu.backend.course.dto;

import java.util.List;

public record CourseViewDto(String name, String code, Integer credit,
                            String description, List<StudentForCourseDetailsDto> students) {
}
