package pl.edu.university.student.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.student.dto.StudentCreateDto;
import pl.edu.university.student.dto.StudentPreviewDto;
import pl.edu.university.student.dto.StudentUpdateDto;
import pl.edu.university.student.dto.StudentViewDto;
import pl.edu.university.student.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentPreviewDto> getAllCoursesPreview() {
        return studentService.getAllPreview();
    }

    @GetMapping("/details/{id}")
    public StudentViewDto getCourseDetails(@PathVariable(name = "id") Integer id) {
        return studentService.getStudentDetails(id);
    }

    @GetMapping("/{id}")
    public StudentPreviewDto getCoursePreview(@PathVariable(name = "id") Integer id) {
        return studentService.getStudentPreview(id);
    }

    @PutMapping("/{id}")
    public StudentViewDto updateCourse(
            @PathVariable(name = "id") Integer id,
            @RequestBody @Valid StudentUpdateDto dto
    ) {
        return studentService.updateStudent(id, dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentViewDto createCourse(
            @RequestBody @Valid StudentCreateDto dto
    ) {
        return studentService.createStudent(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Integer id) {
        studentService.deleteStudent(id);
    }
}
