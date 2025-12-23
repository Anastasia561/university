package pl.edu.university.enrollment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.enrollment.dto.EnrollmentCreateDto;
import pl.edu.university.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.university.enrollment.dto.EnrollmentViewDto;
import pl.edu.university.enrollment.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @GetMapping
    public List<EnrollmentPreviewDto> getAllEnrollmentsPreview() {
        return enrollmentService.getAllPreview();
    }

    @GetMapping("/details/{id}")
    public EnrollmentViewDto getEnrollmentDetails(@PathVariable(name = "id") Integer id) {
        return enrollmentService.getEnrollmentDetails(id);
    }

    @GetMapping("/{id}")
    public EnrollmentPreviewDto getEnrollmentPreview(@PathVariable(name = "id") Integer id) {
        return enrollmentService.getEnrollmentPreview(id);
    }

    @PutMapping("/{id}")
    public EnrollmentViewDto updateCourse(
            @PathVariable(name = "id") Integer id,
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return enrollmentService.updateEnrollment(id, dto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentViewDto createCourse(
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return enrollmentService.createEnrollment(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Integer id) {
        enrollmentService.deleteEnrollment(id);
    }
}
