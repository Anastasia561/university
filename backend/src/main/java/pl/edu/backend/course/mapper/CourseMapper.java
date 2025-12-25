package pl.edu.backend.course.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.backend.course.dto.CourseCreateDto;
import pl.edu.backend.course.dto.CoursePreviewDto;
import pl.edu.backend.course.dto.CourseUpdateDto;
import pl.edu.backend.course.dto.CourseViewDto;
import pl.edu.backend.course.dto.StudentForCourseDetailsDto;
import pl.edu.backend.course.model.Course;
import pl.edu.backend.enrollment.model.Enrollment;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    @Mapping(source = "uuid", target = "id")
    CoursePreviewDto toCoursePreviewDto(Course course);

    @Mapping(source = "enrollments", target = "students")
    CourseViewDto toCourseViewDto(Course course);

    Course toEntity(CourseCreateDto courseCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CourseUpdateDto dto, @MappingTarget Course entity);

    @Mapping(source = "student.email", target = "email")
    StudentForCourseDetailsDto enrollmentToStudentForCourseDetailsDto(Enrollment enrollment);
}
