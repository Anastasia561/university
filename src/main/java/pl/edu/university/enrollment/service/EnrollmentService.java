package pl.edu.university.enrollment.service;

import pl.edu.university.enrollment.dto.EnrollmentCreateDto;
import pl.edu.university.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.university.enrollment.dto.EnrollmentViewDto;

import java.util.List;

public interface EnrollmentService {
    List<EnrollmentPreviewDto> getAllPreview();

    EnrollmentViewDto getEnrollmentDetails(Integer id);

    EnrollmentPreviewDto getEnrollmentPreview(Integer id);

    EnrollmentViewDto createEnrollment(EnrollmentCreateDto dto);

    EnrollmentViewDto updateEnrollment(Integer id, EnrollmentCreateDto dto);

    void deleteEnrollment(Integer id);
}
