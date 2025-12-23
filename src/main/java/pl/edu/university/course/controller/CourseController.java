package pl.edu.university.course.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.course.dto.CourseCreateDto;
import pl.edu.university.course.dto.CoursePreviewDto;
import pl.edu.university.course.dto.CourseUpdateDto;
import pl.edu.university.course.dto.CourseViewDto;
import pl.edu.university.course.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;

    @GetMapping
    public List<CoursePreviewDto> getAllCoursesPreview() {
        return courseService.getAllPreview();
    }

    @GetMapping("/details/{id}")
    public CourseViewDto getCourseDetails(@PathVariable Integer id) {
        return courseService.getCourseDetails(id);
    }

    @GetMapping("/{id}")
    public CoursePreviewDto getCoursePreview(@PathVariable Integer id) {
        return courseService.getCoursePreview(id);
    }

    @PutMapping("/{id}")
    public CourseViewDto updateCourse(
            @PathVariable(name = "id") Integer id,
            @RequestBody @Valid CourseUpdateDto dto
    ) {
        return courseService.updateCourse(id, dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseViewDto createCourse(
            @RequestBody @Valid CourseCreateDto dto
    ) {
        return courseService.createCourse(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
    }
}
