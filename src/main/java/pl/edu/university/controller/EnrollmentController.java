package pl.edu.university.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.university.model.dtos.enrollment.EnrollmentCreateDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentPreviewDto;
import pl.edu.university.model.dtos.enrollment.EnrollmentViewDto;
import pl.edu.university.service.EnrollmentService;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<List<EnrollmentPreviewDto>> getAllEnrollmentsPreview() {
        return ResponseEntity.ok(enrollmentService.getAllPreview());
    }

    @GetMapping("/details/{id}")
    public ResponseEntity<EnrollmentViewDto> getEnrollmentDetails(@PathVariable Integer id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentDetails(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentPreviewDto> getEnrollmentPreview(@PathVariable Integer id) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentPreview(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentViewDto> updateCourse(
            @PathVariable Integer id,
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return ResponseEntity.ok(enrollmentService.updateEnrollment(id, dto));
    }

    @PostMapping
    public ResponseEntity<EnrollmentViewDto> createCourse(
            @RequestBody @Valid EnrollmentCreateDto dto
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(enrollmentService.createEnrollment(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Integer id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.noContent().build();
    }
}
