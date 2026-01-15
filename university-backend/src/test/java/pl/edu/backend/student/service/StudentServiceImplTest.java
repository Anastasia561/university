package pl.edu.backend.student.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.backend.auth.core.CustomUserDetails;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.mapper.StudentMapper;
import pl.edu.backend.student.model.Student;
import pl.edu.backend.student.repository.StudentRepository;
import pl.edu.backend.user.model.Role;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceImplTest {
    @Mock
    private StudentMapper studentMapper;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentServiceImpl studentService;

    @Test
    void shouldReturnAllStudentsAsPreviewDtoNonPageable_whenStudentsExist() {
        Student s1 = new Student();
        Student s2 = new Student();

        StudentPreviewDto dto1 = new StudentPreviewDto(UUID.randomUUID(), "f", "l", "e");
        StudentPreviewDto dto2 = new StudentPreviewDto(UUID.randomUUID(), "f2", "l2", "e2");

        when(studentRepository.findAll()).thenReturn(List.of(s1, s2));
        when(studentMapper.toStudentPreviewDto(s1)).thenReturn(dto1);
        when(studentMapper.toStudentPreviewDto(s2)).thenReturn(dto2);

        List<StudentPreviewDto> result = studentService.getAllPreviewNonPageable();

        assertThat(result).hasSize(2).containsExactly(dto1, dto2);

        verify(studentRepository).findAll();
        verify(studentMapper).toStudentPreviewDto(s1);
        verify(studentMapper).toStudentPreviewDto(s2);
    }

    @Test
    void shouldReturnPagedStudentPreviews_whenStudentsExist() {
        int page = 0;
        int size = 2;

        Student s1 = new Student();
        Student s2 = new Student();

        StudentPreviewDto dto1 = new StudentPreviewDto(UUID.randomUUID(), "f", "l", "e");
        StudentPreviewDto dto2 = new StudentPreviewDto(UUID.randomUUID(), "f2", "l2", "e2");

        Pageable expectedPageable = PageRequest.of(page, size, Sort.by("firstName").ascending());

        Page<Student> studentPage = new PageImpl<>(List.of(s1, s2), expectedPageable, 2);

        when(studentRepository.findAll(any(Pageable.class))).thenReturn(studentPage);
        when(studentMapper.toStudentPreviewDto(s1)).thenReturn(dto1);
        when(studentMapper.toStudentPreviewDto(s2)).thenReturn(dto2);

        Page<StudentPreviewDto> result = studentService.getAllPreviewPageable(page, size);

        assertThat(result.getContent()).containsExactly(dto1, dto2);

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getNumber()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(2);

        verify(studentRepository).findAll(any(Pageable.class));
    }

    @Test
    void shouldReturnStudentViewDto_whenInputIsValid() {
        UUID uuid = UUID.randomUUID();

        Student student = new Student();
        StudentViewDto dto = new StudentViewDto("f", "l", "e", LocalDate.now(), List.of());

        when(studentRepository.findByUuid(uuid)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentViewDto(student)).thenReturn(dto);

        StudentViewDto result = studentService.getStudentDetails(uuid);

        assertThat(result).isSameAs(dto);

        verify(studentRepository).findByUuid(uuid);
        verify(studentMapper).toStudentViewDto(student);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenStudentDoesNotExist() {
        UUID uuid = UUID.randomUUID();

        when(studentRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.getStudentDetails(uuid)
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).findByUuid(uuid);
        verifyNoInteractions(studentMapper);
    }

    @Test
    void shouldReturnStudent_whenEmailExists() {
        String email = "john@mail.com";
        Student student = new Student();

        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));

        Student result = studentService.getStudentByEmail(email);

        assertThat(result).isSameAs(student);
        verify(studentRepository).findByEmail(email);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenEmailDoesNotExist() {
        String email = "missing@mail.com";

        when(studentRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.getStudentByEmail(email)
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).findByEmail(email);
    }

    @Test
    void shouldReturnStudentPreview_whenInputIsValid() {
        UUID uuid = UUID.randomUUID();
        Student student = new Student();
        StudentPreviewDto dto = new StudentPreviewDto(uuid, "f", "l", "e");

        when(studentRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(student));
        when(studentMapper.toStudentPreviewDto(student)).thenReturn(dto);
        StudentPreviewDto result = studentService.getStudentPreview(uuid);

        assertThat(result).isSameAs(dto);
        verify(studentRepository).findByUuid(uuid);
        verify(studentMapper).toStudentPreviewDto(student);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenStudentDoesNotExistForPreview() {
        UUID uuid = UUID.randomUUID();

        when(studentRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.getStudentPreview(uuid)
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).findByUuid(uuid);
    }

    @Test
    void shouldReturnStudentProfile_whenInputIsValid() {
        mockAuth();

        Student student = new Student();
        StudentProfileDto dto = new StudentProfileDto("f", "l", "e", LocalDate.now());

        when(studentRepository.findByEmail("student@example.com")).thenReturn(Optional.of(student));
        when(studentMapper.toStudentProfileDto(student)).thenReturn(dto);

        StudentProfileDto result = studentService.getStudentProfile();

        assertThat(result).isSameAs(dto);

        verify(studentRepository).findByEmail("student@example.com");
        verify(studentMapper).toStudentProfileDto(student);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenStudentDoesNotExistForProfile() {
        mockAuth();
        when(studentRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.getStudentProfile()
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).findByEmail("student@example.com");
    }

    @Test
    void shouldCreateStudent_whenInputIsValid() {
        StudentCreateDto dto = new StudentCreateDto("John", "test", "email",
                LocalDate.of(1970, 1, 1));

        Student mappedStudent = new Student();
        Student savedStudent = new Student();
        StudentViewDto viewDto = new StudentViewDto("John", "test", "email",
                LocalDate.of(1970, 1, 1), List.of());

        when(studentMapper.toStudent(dto, passwordEncoder)).thenReturn(mappedStudent);
        when(studentRepository.save(mappedStudent)).thenReturn(savedStudent);
        when(studentMapper.toStudentViewDto(savedStudent)).thenReturn(viewDto);

        StudentViewDto result = studentService.createStudent(dto);

        assertThat(result).isSameAs(viewDto);

        verify(studentMapper).toStudent(dto, passwordEncoder);
        verify(studentRepository).save(mappedStudent);
        verify(studentMapper).toStudentViewDto(savedStudent);
    }

    @Test
    void shouldUpdateStudent_whenEmailUnchanged() {
        UUID id = UUID.randomUUID();
        StudentUpdateDto dto = new StudentUpdateDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1));

        Student student = new Student();
        student.setEmail("student@mail.com");
        StudentViewDto viewDto = new StudentViewDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1), List.of());

        when(studentRepository.findByUuid(id)).thenReturn(Optional.of(student));
        when(studentMapper.toStudentViewDto(student)).thenReturn(viewDto);

        StudentViewDto result = studentService.updateStudent(id, dto);

        assertThat(result).isSameAs(viewDto);

        verify(studentRepository).findByUuid(id);
        verify(studentMapper).updateFromDto(dto, student);
        verify(studentMapper).toStudentViewDto(student);
        verify(studentRepository, never()).existsByEmail(anyString());
    }

    @Test
    void shouldUpdateStudent_whenEmailChangedAndUnique() {
        UUID id = UUID.randomUUID();
        StudentUpdateDto dto = new StudentUpdateDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1));

        Student student = new Student();
        student.setEmail("old@mail.com");
        StudentViewDto viewDto = new StudentViewDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1), List.of());

        when(studentRepository.findByUuid(id)).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmail(any(String.class))).thenReturn(false);
        when(studentMapper.toStudentViewDto(student)).thenReturn(viewDto);

        StudentViewDto result = studentService.updateStudent(id, dto);

        assertThat(result).isSameAs(viewDto);

        verify(studentRepository).findByUuid(id);
        verify(studentRepository).existsByEmail("student@mail.com");
        verify(studentMapper).updateFromDto(dto, student);
        verify(studentMapper).toStudentViewDto(student);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenEmailAlreadyExists() {
        UUID id = UUID.randomUUID();
        StudentUpdateDto dto = new StudentUpdateDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1));

        Student student = new Student();
        student.setEmail("old@mail.com");

        when(studentRepository.findByUuid(any(UUID.class))).thenReturn(Optional.of(student));
        when(studentRepository.existsByEmail(any(String.class))).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.updateStudent(id, dto));

        assertEquals("Student email must be unique", exception.getMessage());

        verify(studentRepository).findByUuid(id);
        verify(studentRepository).existsByEmail("student@mail.com");
        verifyNoInteractions(studentMapper);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenStudentDoesNotExistForUpdate() {
        UUID uuid = UUID.randomUUID();
        StudentUpdateDto dto = new StudentUpdateDto("John", "test", "student@mail.com",
                LocalDate.of(1970, 1, 1));

        when(studentRepository.findByUuid(any(UUID.class))).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.updateStudent(uuid, dto)
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).findByUuid(uuid);
    }

    @Test
    void shouldDeleteStudent_whenStudentExists() {
        UUID id = UUID.randomUUID();

        when(studentRepository.existsByUuid(id)).thenReturn(true);

        studentService.deleteStudent(id);

        verify(studentRepository).existsByUuid(id);
        verify(studentRepository).deleteByUuid(id);
    }

    @Test
    void shouldThrowEntityNotFoundException_whenStudentDoesNotExistForDelete() {
        UUID uuid = UUID.randomUUID();

        when(studentRepository.existsByUuid(any(UUID.class))).thenReturn(false);

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> studentService.deleteStudent(uuid)
        );

        assertEquals("Student not found", exception.getMessage());

        verify(studentRepository).existsByUuid(uuid);
    }

    @Test
    void shouldRegisterStudent_whenInputIsValid() {
        StudentRegisterDto dto = new StudentRegisterDto("John", "test", "email",
                "pass", "pass", LocalDate.of(1970, 1, 1));

        Student mappedStudent = new Student();
        Student savedStudent = new Student();
        StudentViewDto viewDto = new StudentViewDto("John", "test", "email",
                LocalDate.of(1970, 1, 1), List.of());

        when(studentMapper.toStudentForRegistration(dto, passwordEncoder)).thenReturn(mappedStudent);
        when(studentRepository.save(mappedStudent)).thenReturn(savedStudent);
        when(studentMapper.toStudentViewDto(savedStudent)).thenReturn(viewDto);

        StudentViewDto result = studentService.registerStudent(dto);

        assertThat(result).isSameAs(viewDto);

        verify(studentMapper).toStudentForRegistration(dto, passwordEncoder);
        verify(studentRepository).save(mappedStudent);
        verify(studentMapper).toStudentViewDto(savedStudent);
    }

    @Test
    void shouldThrowIllegalArgumentException_whenPasswordsDoNotMatch() {
        StudentRegisterDto dto = new StudentRegisterDto("John", "test", "email",
                "pass1", "pass2", LocalDate.of(1970, 1, 1));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> studentService.registerStudent(dto)
        );

        assertEquals("Passwords do not match", exception.getMessage());

        verifyNoInteractions(studentRepository, studentMapper);
    }

    private void mockAuth() {
        CustomUserDetails userDetails = new CustomUserDetails(
                1,
                "student@example.com",
                "hashedPassword",
                Role.STUDENT
        );

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
