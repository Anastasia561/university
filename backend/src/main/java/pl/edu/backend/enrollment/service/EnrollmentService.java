package pl.edu.backend.enrollment.service;


import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentPreviewDto> getAllPreview();

    EnrollmentViewDto getEnrollmentDetails(Integer id);

    EnrollmentPreviewDto getEnrollmentPreview(Integer id);

    EnrollmentViewDto createEnrollment(EnrollmentCreateDto dto);

    EnrollmentViewDto updateEnrollment(Integer id, EnrollmentCreateDto dto);

    void deleteEnrollment(Integer id);
}
