package pl.edu.backend.enrollment.service;


import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;

import java.util.List;
import java.util.UUID;

public interface EnrollmentService {
    List<EnrollmentPreviewDto> getAllPreview();

    EnrollmentViewDto getEnrollmentDetails(UUID id);

    EnrollmentPreviewDto getEnrollmentPreview(UUID id);

    EnrollmentViewDto createEnrollment(EnrollmentCreateDto dto);

    EnrollmentViewDto updateEnrollment(UUID id, EnrollmentCreateDto dto);

    void deleteEnrollment(UUID id);
}
