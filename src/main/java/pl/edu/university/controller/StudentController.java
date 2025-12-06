package pl.edu.university.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.model.dtos.student.StudentCreateDto;
import pl.edu.university.model.dtos.student.StudentPreviewDto;
import pl.edu.university.model.dtos.student.StudentViewDto;
import pl.edu.university.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentPreviewDto>> getAllCoursesPreview() {
        return ResponseEntity.ok(studentService.getAllPreview());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<StudentViewDto> getCourseDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentDetails(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentPreviewDto> getCoursePreview(@PathVariable Integer id) {
        return ResponseEntity.ok(studentService.getStudentPreview(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentViewDto> updateCourse(
            @PathVariable Integer id,
            @RequestBody @Valid StudentCreateDto dto
    ) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @PostMapping
    public ResponseEntity<StudentViewDto> createCourse(
            @RequestBody @Valid StudentCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(studentService.createStudent(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
