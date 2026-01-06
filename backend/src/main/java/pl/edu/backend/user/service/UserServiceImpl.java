package pl.edu.backend.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.model.User;
import pl.edu.backend.user.repository.UserRepository;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserProfileDto getUserProfile(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .map(studentMapper::toStudentProfileDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
