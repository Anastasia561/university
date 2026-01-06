package pl.edu.backend.user.mapper;

import org.mapstruct.Mapper;
import pl.edu.backend.student.dto.StudentProfileDto;
import pl.edu.backend.student.model.Student;

@Mapper(componentModel = "spring")
public interface UserMapper {
    StudentProfileDto toUserProfileDto(Student student);
}
