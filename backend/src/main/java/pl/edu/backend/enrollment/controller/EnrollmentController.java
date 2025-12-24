package pl.edu.backend.enrollment.controller;

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
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.service.EnrollmentService;

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
