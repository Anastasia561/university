package pl.edu.backend.user.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.edu.backend.user.dto.PasswordDto;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.mapper.UserMapper;
import pl.edu.backend.user.model.User;
import pl.edu.backend.user.repository.UserRepository;
import pl.edu.backend.validation.annotation.Password;

@Service
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    public UserProfileDto getUserProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        return userRepository.findByEmail(email)
                .map(userMapper::toUserProfileDto)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void changePassword(PasswordDto dto) {
        if (!dto.password().equals(dto.repeatPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(dto.password()));
    }
}
