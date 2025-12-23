package pl.edu.university.student.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.university.student.dto.StudentUpdateDto;
import pl.edu.university.student.mapper.StudentMapper;
import pl.edu.university.student.dto.StudentCreateDto;
import pl.edu.university.student.dto.StudentPreviewDto;
import pl.edu.university.student.dto.StudentViewDto;
import pl.edu.university.student.model.Student;
import pl.edu.university.student.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentPreviewDto> getAllPreview() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toStudentPreviewDto)
                .collect(Collectors.toList());
    }

    @Override
    public StudentViewDto getStudentDetails(Integer id) {
        return studentRepository.findById(id)
                .map(studentMapper::toStudentViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Override
    public StudentPreviewDto getStudentPreview(Integer id) {
        return studentRepository.findById(id)
                .map(studentMapper::toStudentPreviewDto)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
    }

    @Override
    @Transactional
    public StudentViewDto createStudent(StudentCreateDto dto) {
        Student student = studentMapper.toStudent(dto);
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentViewDto(saved);
    }

    @Override
    @Transactional
    public StudentViewDto updateStudent(Integer id, StudentUpdateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!student.getEmail().equals(dto.email()) && studentRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Student email must be unique");
        }
        studentMapper.updateFromDto(dto, student);

        return studentMapper.toStudentViewDto(student);
    }

    @Override
    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        studentRepository.deleteById(id);
    }
}
