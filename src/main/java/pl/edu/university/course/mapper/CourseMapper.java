package pl.edu.university.course.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import pl.edu.university.course.dto.CourseUpdateDto;
import pl.edu.university.course.dto.StudentForCourseDetailsDto;
import pl.edu.university.course.model.Course;
import pl.edu.university.course.dto.CourseCreateDto;
import pl.edu.university.course.dto.CoursePreviewDto;
import pl.edu.university.course.dto.CourseViewDto;
import pl.edu.university.enrollment.model.Enrollment;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    CoursePreviewDto toCoursePreviewDto(Course course);

    @Mapping(source = "enrollments", target = "students")
    CourseViewDto toCourseViewDto(Course course);

    Course toEntity(CourseCreateDto courseCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(CourseUpdateDto dto, @MappingTarget Course entity);

    @Mapping(source = "student.email", target = "email")
    StudentForCourseDetailsDto enrollmentToStudentForCourseDetailsDto(Enrollment enrollment);
}
