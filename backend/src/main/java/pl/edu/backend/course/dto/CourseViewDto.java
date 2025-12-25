package pl.edu.backend.course.dto;

import java.util.List;
import java.util.UUID;

public record CourseViewDto(UUID id, String name, String code, Integer credit,
                            String description, List<StudentForCourseDetailsDto> students) {
}
