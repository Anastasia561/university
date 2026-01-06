package pl.edu.backend.student.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<StudentPreviewDto>> getAllStudentsPreviewPageable(@RequestParam(defaultValue = "0") int page,
                                                                                 @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(studentService.getAllPreviewPageable(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<StudentPreviewDto> getAllStudentsPreviewNonPageable() {
        return studentService.getAllPreviewNonPageable();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/details/{id}")
    public StudentViewDto getStudentDetails(@PathVariable(name = "id") UUID id) {
        return studentService.getStudentDetails(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public StudentPreviewDto getStudentPreview(@PathVariable(name = "id") UUID id) {
        return studentService.getStudentPreview(id);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/profile")
    public StudentProfileDto getStudentProfile() {
        return studentService.getStudentProfile();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public StudentViewDto updateStudent(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid StudentUpdateDto dto
    ) {
        return studentService.updateStudent(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentViewDto createStudent(
            @RequestBody @Valid StudentCreateDto dto
    ) {
        return studentService.createStudent(dto);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public StudentViewDto registerStudent(
            @RequestBody @Valid StudentRegisterDto dto
    ) {
        return studentService.registerStudent(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable(name = "id") UUID id) {
        studentService.deleteStudent(id);
    }
}
