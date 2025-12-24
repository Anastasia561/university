package pl.edu.backend.course.dto;

import java.util.List;

public record CourseViewDto(Integer id, String name, String code, Integer credit,
                            String description, List<StudentForCourseDetailsDto> students) {
}
