package pl.edu.backend.enrollment.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.backend.enrollment.dto.EnrollmentCreateDto;
import pl.edu.backend.enrollment.dto.EnrollmentPreviewDto;
import pl.edu.backend.enrollment.dto.EnrollmentViewDto;
import pl.edu.backend.enrollment.model.Enrollment;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    @Mapping(source = "enrollmentDate", target = "date")
    Enrollment toEnrollment(EnrollmentCreateDto dto);

    @Mapping(source = "uuid", target = "id")
    @Mapping(source = "course.code", target = "courseCode")
    @Mapping(source = "student.email", target = "studentEmail")
    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentPreviewDto toEnrollmentPreviewDto(Enrollment enrollment);

    @Mapping(source = "date", target = "enrollmentDate")
    EnrollmentViewDto toEnrollmentViewDto(Enrollment enrollment);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(EnrollmentCreateDto dto, @MappingTarget Enrollment entity);
}
