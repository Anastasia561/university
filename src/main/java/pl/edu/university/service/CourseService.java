package pl.edu.university.service;

import org.springframework.stereotype.Service;
import pl.edu.university.exception.CourseNotFoundException;
import pl.edu.university.mapper.CourseMapper;
import pl.edu.university.model.Course;
import pl.edu.university.model.dtos.CourseCreateDto;
import pl.edu.university.model.dtos.CoursePreviewDto;
import pl.edu.university.model.dtos.CourseViewDto;
import pl.edu.university.repository.CourseRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper) {
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    public List<CoursePreviewDto> getAllPreview() {
        return courseRepository.findAll().stream()
                .map(courseMapper::toCoursePreviewDto)
                .collect(Collectors.toList());
    }

    public CourseViewDto getCourseDetails(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toCourseViewDto)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    public CoursePreviewDto getCoursePreview(Integer courseId) {
        return courseRepository.findById(courseId)
                .map(courseMapper::toCoursePreviewDto)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
    }

    public CourseViewDto createCourse(CourseCreateDto dto) {
        Course course = courseMapper.toCourse(dto);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toCourseViewDto(savedCourse);
    }

    public CourseViewDto updateCourse(Integer courseId, CourseCreateDto dto) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        course.setName(dto.getCourseName());
        course.setCode(dto.getCourseCode());
        course.setCredit(dto.getCredit());
        course.setDescription(dto.getDescription());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toCourseViewDto(updatedCourse);
    }

    public void deleteCourse(Integer courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        courseRepository.deleteById(courseId);
    }
}
