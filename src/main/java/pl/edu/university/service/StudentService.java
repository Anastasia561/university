package pl.edu.university.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.university.exception.CourseNotFoundException;
import pl.edu.university.exception.StudentNotFoundException;
import pl.edu.university.mapper.StudentMapper;
import pl.edu.university.model.dtos.student.StudentCreateDto;
import pl.edu.university.model.dtos.student.StudentPreviewDto;
import pl.edu.university.model.dtos.student.StudentViewDto;
import pl.edu.university.model.entity.Student;
import pl.edu.university.repository.StudentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    public List<StudentPreviewDto> getAllPreview() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toStudentPreviewDto)
                .collect(Collectors.toList());
    }

    public StudentViewDto getStudentDetails(Integer id) {
        return studentRepository.findById(id)
                .map(studentMapper::toStudentViewDto)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    public StudentPreviewDto getStudentPreview(Integer id) {
        return studentRepository.findById(id)
                .map(studentMapper::toStudentPreviewDto)
                .orElseThrow(() -> new CourseNotFoundException(id));
    }

    @Transactional
    public StudentViewDto createStudent(StudentCreateDto dto) {
        Student student = studentMapper.toStudent(dto);
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentViewDto(saved);
    }

    @Transactional
    public StudentViewDto updateStudent(Integer id, StudentCreateDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));

        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setEmail(dto.getEmail());
        student.setBirthdate(dto.getBirthDate());

        return studentMapper.toStudentViewDto(student);
    }

    @Transactional
    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new StudentNotFoundException(id);
        }

        studentRepository.deleteById(id);
    }
}
