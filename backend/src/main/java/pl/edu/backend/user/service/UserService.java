package pl.edu.backend.user.service;

import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.model.User;

public interface UserService {
    User findById(Integer id);

    UserProfileDto getUserProfile();
}
