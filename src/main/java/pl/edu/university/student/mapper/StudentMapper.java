package pl.edu.university.student.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.university.student.dto.EnrollmentForStudentDto;
import pl.edu.university.student.dto.StudentCreateDto;
import pl.edu.university.student.dto.StudentPreviewDto;
import pl.edu.university.student.dto.StudentUpdateDto;
import pl.edu.university.student.dto.StudentViewDto;
import pl.edu.university.enrollment.model.Enrollment;
import pl.edu.university.student.model.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentPreviewDto toStudentPreviewDto(Student student);

    StudentViewDto toStudentViewDto(Student student);

    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentForStudentDto enrollmentToEnrollmentForStudentDto(Enrollment enrollment);

    @Mapping(target = "password", constant = "111")
    @Mapping(target = "role", constant = "STUDENT")
    Student toStudent(StudentCreateDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(StudentUpdateDto dto, @MappingTarget Student entity);
}
