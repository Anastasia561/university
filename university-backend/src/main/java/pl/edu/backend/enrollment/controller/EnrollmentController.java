package pl.edu.backend.enrollment.controller;

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
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.service.EnrollmentService;

import java.util.UUID;

@RestController
@RequestMapping("/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @GetMapping
    public ResponseEntity<Page<EnrollmentPreviewDto>> getAllEnrollmentsPreview(@RequestParam(defaultValue = "0") int page,
                                                                               @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(enrollmentService.getAllPreview(page, size));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/details/{id}")
    public EnrollmentViewDto getEnrollmentDetails(@PathVariable(name = "id") UUID id) {
        return enrollmentService.getEnrollmentDetails(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EnrollmentPreviewDto getEnrollmentPreview(@PathVariable(name = "id") UUID id) {
        return enrollmentService.getEnrollmentPreview(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EnrollmentPreviewDto updateCourse(
            @PathVariable(name = "id") UUID id,
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return enrollmentService.updateEnrollment(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentPreviewDto createCourse(
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return enrollmentService.createEnrollment(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable(name = "id") UUID id) {
        enrollmentService.deleteEnrollment(id);
    }
}
