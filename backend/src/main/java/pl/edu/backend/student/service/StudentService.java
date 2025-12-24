package pl.edu.backend.student.service;


import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;

import java.util.List;

public interface StudentService {
    List<StudentPreviewDto> getAllPreview();

    StudentViewDto getStudentDetails(Integer id);

    StudentPreviewDto getStudentPreview(Integer id);

    StudentViewDto createStudent(StudentCreateDto dto);

    StudentViewDto updateStudent(Integer id, StudentUpdateDto dto);

    void deleteStudent(Integer id);
}
