package pl.edu.backend.user.mapper;

import org.mapstruct.Mapper;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserProfileDto toUserProfileDto(User user);
}
