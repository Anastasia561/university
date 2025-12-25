package pl.edu.backend.course.service;

import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;

import java.util.List;
import java.util.UUID;

public interface CourseService {
    List<CoursePreviewDto> getAllPreview();

    CourseViewDto getCourseDetails(UUID courseId);

    CoursePreviewDto getCoursePreview(UUID courseId);

    CourseViewDto createCourse(CourseCreateDto dto);

    CourseViewDto updateCourse(UUID courseId, CourseUpdateDto dto);

    void deleteCourse(UUID courseId);
}
