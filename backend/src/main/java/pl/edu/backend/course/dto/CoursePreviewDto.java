package pl.edu.backend.course.dto;

import java.util.UUID;

public record CoursePreviewDto(UUID id, String name,
                               String code, Integer credit) {
}
