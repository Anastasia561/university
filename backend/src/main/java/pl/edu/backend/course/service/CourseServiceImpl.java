package pl.edu.backend.course.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;
import pl.edu.backend.course.mapper.CourseMapper;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.course.repository.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public List<CoursePreviewDto> getAllPreview() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toCoursePreviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public CourseViewDto getCourseDetails(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toCourseViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Override
    public CoursePreviewDto getCoursePreview(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toCoursePreviewDto)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public CourseViewDto createCourse(CourseCreateDto dto) {
        Course course = courseMapper.toEntity(dto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseViewDto(savedCourse);
    }

    @Override
    @Transactional
    public CourseViewDto updateCourse(Integer courseId, CourseUpdateDto dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!course.getCode().equals(dto.code()) && courseRepository.existsByCode(dto.code())) {
            throw new IllegalArgumentException("Course code must be unique");
        }
        courseMapper.updateFromDto(dto, course);
        return courseMapper.toCourseViewDto(course);
    }

    @Override
    public void deleteCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found");
        }
        courseRepository.deleteById(courseId);
    }
}
