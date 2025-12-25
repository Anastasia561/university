package pl.edu.backend.student.service;


import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    List<StudentPreviewDto> getAllPreview();

    StudentViewDto getStudentDetails(UUID id);

    StudentPreviewDto getStudentPreview(UUID id);

    StudentViewDto createStudent(StudentCreateDto dto);

    StudentViewDto updateStudent(UUID id, StudentUpdateDto dto);

    void deleteStudent(UUID id);
}
