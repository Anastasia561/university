package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.model.entity.Course;
import pl.edu.university.model.dtos.course.CourseCreateDto;
import pl.edu.university.model.dtos.course.CoursePreviewDto;
import pl.edu.university.model.dtos.course.CourseViewDto;
import pl.edu.university.model.dtos.course.StudentForCourseDetailsDto;
import pl.edu.university.repository.EnrollmentRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CourseMapper {
    private final EnrollmentRepository enrollmentRepository;

    public CourseMapper(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public CoursePreviewDto toCoursePreviewDto(Course c) {
        return CoursePreviewDto.builder()
                .id(c.getId())
                .courseName(c.getName())
                .courseCode(c.getCode())
                .credit(c.getCredit())
                .build();
    }

    public CourseViewDto toCourseViewDto(Course c) {
        CourseViewDto dto = CourseViewDto.builder()
                .id(c.getId())
                .courseName(c.getName())
                .courseCode(c.getCode())
                .credit(c.getCredit())
                .description(c.getDescription())
                .build();

        List<StudentForCourseDetailsDto> studentDetailsDtos = enrollmentRepository.findByCourse(c).stream()
                .map(e -> {
                    return StudentForCourseDetailsDto.builder()
                            .studentEmail(e.getStudent().getEmail())
                            .finalGrade(e.getFinalGrade())
                            .build();
                }).collect(Collectors.toList());

        dto.setStudents(studentDetailsDtos);
        return dto;
    }

    public Course toCourse(CourseCreateDto dto) {
        return Course.builder()
                .code(dto.getCourseCode())
                .name(dto.getCourseName())
                .credit(dto.getCredit())
                .description(dto.getDescription())
                .build();
    }
}
