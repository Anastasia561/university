package pl.edu.university.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.model.dtos.course.CourseCreateDto;
import pl.edu.university.model.dtos.course.CoursePreviewDto;
import pl.edu.university.model.dtos.course.CourseViewDto;
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
    public ResponseEntity<List<CoursePreviewDto>> getAllCoursesPreview() {
        return ResponseEntity.ok(courseService.getAllPreview());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<CourseViewDto> getCourseDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCourseDetails(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoursePreviewDto> getCoursePreview(@PathVariable Integer id) {
        return ResponseEntity.ok(courseService.getCoursePreview(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseViewDto> updateCourse(
            @PathVariable Integer id,
            @RequestBody @Valid CourseCreateDto dto
    ) {
        return ResponseEntity.ok(courseService.updateCourse(id, dto));
    }

    @PostMapping
    public ResponseEntity<CourseViewDto> createCourse(
            @RequestBody @Valid CourseCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(courseService.createCourse(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
