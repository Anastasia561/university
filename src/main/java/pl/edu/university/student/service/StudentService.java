package pl.edu.university.student.service;

import pl.edu.university.student.dto.StudentCreateDto;
import pl.edu.university.student.dto.StudentPreviewDto;
import pl.edu.university.student.dto.StudentUpdateDto;
import pl.edu.university.student.dto.StudentViewDto;

import java.util.List;

public interface StudentService {
    List<StudentPreviewDto> getAllPreview();

    StudentViewDto getStudentDetails(Integer id);

    StudentPreviewDto getStudentPreview(Integer id);

    StudentViewDto createStudent(StudentCreateDto dto);

    StudentViewDto updateStudent(Integer id, StudentUpdateDto dto);

    void deleteStudent(Integer id);
}
