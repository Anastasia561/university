package pl.edu.backend.enrollment.service;

import org.springframework.data.domain.Page;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;

import java.util.UUID;

public interface EnrollmentService {
    Page<EnrollmentPreviewDto> getAllPreview(int page, int size);

    EnrollmentViewDto getEnrollmentDetails(UUID id);

    EnrollmentPreviewDto getEnrollmentPreview(UUID id);

    EnrollmentPreviewDto createEnrollment(EnrollmentCreateDto dto);

    EnrollmentPreviewDto updateEnrollment(UUID id, EnrollmentCreateDto dto);

    void deleteEnrollment(UUID id);
}
