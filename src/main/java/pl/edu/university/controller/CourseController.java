package pl.edu.university.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.model.dtos.CourseCreateDto;
import pl.edu.university.model.dtos.CoursePreviewDto;
import pl.edu.university.model.dtos.CourseViewDto;
import pl.edu.university.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

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
    public ResponseEntity<CourseViewDto> updateCourse(
            @PathVariable Integer id,
            @RequestBody @Valid CourseCreateDto dto
    ) {
        CourseViewDto updated = courseService.updateCourse(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<CourseViewDto> createCourse(
            @RequestBody @Valid CourseCreateDto dto
    ) {
        CourseViewDto created = courseService.createCourse(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
