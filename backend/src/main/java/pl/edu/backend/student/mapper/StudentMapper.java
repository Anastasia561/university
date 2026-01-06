package pl.edu.backend.student.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.student.dto.EnrollmentForStudentDto;
import pl.edu.backend.student.dto.StudentCreateDto;
import pl.edu.backend.student.dto.StudentPreviewDto;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.dto.StudentRegisterDto;
import pl.edu.backend.student.dto.StudentUpdateDto;
import pl.edu.backend.student.dto.StudentViewDto;
import pl.edu.backend.student.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    @Mapping(source = "uuid", target = "id")
    StudentPreviewDto toStudentPreviewDto(Student student);

    StudentViewDto toStudentViewDto(Student student);

    StudentProfileDto toStudentProfileDto(Student student);

    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentForStudentDto enrollmentToEnrollmentForStudentDto(Enrollment enrollment);

    @Mapping(target = "password", expression = "java(passwordEncoder.encode(\"111\"))")
    @Mapping(target = "role", constant = "STUDENT")
    Student toStudent(StudentCreateDto dto, PasswordEncoder passwordEncoder);

    @Mapping(target = "role", constant = "STUDENT")
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(dto.password()))")
    Student toStudentForRegistration(StudentRegisterDto dto, PasswordEncoder passwordEncoder);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(StudentUpdateDto dto, @MappingTarget Student entity);
}
