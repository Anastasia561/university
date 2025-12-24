package pl.edu.backend.course.service;

import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;

import java.util.List;

public interface CourseService {
    List<CoursePreviewDto> getAllPreview();

    CourseViewDto getCourseDetails(Integer courseId);

    CoursePreviewDto getCoursePreview(Integer courseId);

    CourseViewDto createCourse(CourseCreateDto dto);

    CourseViewDto updateCourse(Integer courseId, CourseUpdateDto dto);

    void deleteCourse(Integer courseId);
}
