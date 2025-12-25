package pl.edu.backend.student.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.service.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping
    public List<StudentPreviewDto> getAllStudentsPreview() {
        return studentService.getAllPreview();
    }

    @GetMapping("/details/{id}")
    public StudentViewDto getStudentDetails(@PathVariable(name = "id") UUID id) {
        return studentService.getStudentDetails(id);
    }

    @GetMapping("/{id}")
    public StudentPreviewDto getStudentPreview(@PathVariable(name = "id") UUID id) {
        return studentService.getStudentPreview(id);
    }

    @PutMapping("/{id}")
    public StudentViewDto updateStudent(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid StudentUpdateDto dto
    ) {
        return studentService.updateStudent(id, dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentViewDto createStudent(
            @RequestBody @Valid StudentCreateDto dto
    ) {
        return studentService.createStudent(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable(name = "id") UUID id) {
        studentService.deleteStudent(id);
    }
}
