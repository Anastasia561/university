package pl.edu.university.enrollment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.university.enrollment.model.Enrollment;
import pl.edu.university.enrollment.dto.EnrollmentCreateDto;
import pl.edu.university.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.university.enrollment.dto.EnrollmentViewDto;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "enrollmentDate", target = "date")
    Enrollment toEnrollment(EnrollmentCreateDto dto);

    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "student.email", target = "studentEmail")
    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentPreviewDto toEnrollmentPreviewDto(Enrollment enrollment);

    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentViewDto toEnrollmentViewDto(Enrollment enrollment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(EnrollmentCreateDto dto, @MappingTarget Enrollment entity);
}
