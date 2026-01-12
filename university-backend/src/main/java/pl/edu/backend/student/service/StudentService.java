package pl.edu.backend.student.service;


import org.springframework.data.domain.Page;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.model.Student;

import java.util.List;
import java.util.UUID;

public interface StudentService {
    Page<StudentPreviewDto> getAllPreviewPageable(int page, int size);

    List<StudentPreviewDto> getAllPreviewNonPageable();

    StudentViewDto getStudentDetails(UUID id);

    StudentPreviewDto getStudentPreview(UUID id);

    StudentProfileDto getStudentProfile();

    StudentViewDto createStudent(StudentCreateDto dto);

    StudentViewDto registerStudent(StudentRegisterDto dto);

    StudentViewDto updateStudent(UUID id, StudentUpdateDto dto);

    void deleteStudent(UUID id);

    Student getStudentByEmail(String email);
}
