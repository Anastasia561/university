package pl.edu.backend.student.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.mapper.StudentMapper;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.student.repository.StudentRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    @Override
    public List<StudentPreviewDto> getAllPreviewNonPageable() {
        return studentRepository.findAll().stream().map(studentMapper::toStudentPreviewDto).toList();
    }

    @Override
    public Page<StudentPreviewDto> getAllPreviewPageable(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("firstName").ascending());
        return studentRepository.findAll(pageable)
                .map(studentMapper::toStudentPreviewDto);
    }

    @Override
    public StudentViewDto getStudentDetails(UUID id) {
        return studentRepository.findByUuid(id)
                .map(studentMapper::toStudentViewDto)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    @Override
    public StudentPreviewDto getStudentPreview(UUID id) {
        return studentRepository.findByUuid(id)
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
    public StudentViewDto updateStudent(UUID id, StudentUpdateDto dto) {
        Student student = studentRepository.findByUuid(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));

        if (!student.getEmail().equals(dto.email()) && studentRepository.existsByEmail(dto.email())) {
            throw new IllegalArgumentException("Student email must be unique");
        }
        studentMapper.updateFromDto(dto, student);

        return studentMapper.toStudentViewDto(student);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID id) {
        if (!studentRepository.existsByUuid(id)) {
            throw new EntityNotFoundException("Student not found");
        }
        studentRepository.deleteByUuid(id);
    }
}
