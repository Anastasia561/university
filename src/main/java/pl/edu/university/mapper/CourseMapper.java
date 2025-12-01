package pl.edu.university.mapper;

import org.springframework.stereotype.Component;
import pl.edu.university.model.Course;
import pl.edu.university.model.dtos.CourseCreateDto;
import pl.edu.university.model.dtos.CoursePreviewDto;
import pl.edu.university.model.dtos.CourseViewDto;
import pl.edu.university.model.dtos.StudentDetailsDto;
import pl.edu.university.repository.EnrollmentRepository;

import java.util.List;

@Component
public class CourseMapper {
    private final EnrollmentRepository enrollmentRepository;

    public CourseMapper(EnrollmentRepository enrollmentRepository) {
        this.enrollmentRepository = enrollmentRepository;
    }

    public CoursePreviewDto toCoursePreviewDto(Course c) {
        return new CoursePreviewDto(c.getId(), c.getName(), c.getCode(), c.getCredit());
    }

    public CourseViewDto toCourseViewDto(Course c) {
        CourseViewDto courseViewDto = new CourseViewDto(c.getId(), c.getName(),
                c.getCode(), c.getCredit(), c.getDescription());

        List<StudentDetailsDto> studentDetailsDtos = enrollmentRepository.findByCourse(c).stream()
                .map(e ->
                        new StudentDetailsDto(e.getStudent().getEmail(), e.getFinalGrade())).toList();

        courseViewDto.setStudents(studentDetailsDtos);
        return courseViewDto;
    }

    public Course toCourse(CourseCreateDto dto) {
        return new Course(dto.getCourseName(), dto.getCourseCode(),
                dto.getCredit(), dto.getDescription());
    }
}
