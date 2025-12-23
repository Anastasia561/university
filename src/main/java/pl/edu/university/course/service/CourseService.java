package pl.edu.university.course.service;

import pl.edu.university.course.dto.CourseCreateDto;
import pl.edu.university.course.dto.CoursePreviewDto;
import pl.edu.university.course.dto.CourseUpdateDto;
import pl.edu.university.course.dto.CourseViewDto;

import java.util.List;

public interface CourseService {
    List<CoursePreviewDto> getAllPreview();

    CourseViewDto getCourseDetails(Integer courseId);

    CoursePreviewDto getCoursePreview(Integer courseId);

    CourseViewDto createCourse(CourseCreateDto dto);

    CourseViewDto updateCourse(Integer courseId, CourseUpdateDto dto);

    void deleteCourse(Integer courseId);
}
