package pl.edu.backend.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.backend.user.dto.PasswordDto;
import pl.edu.backend.user.dto.UserProfileDto;
import pl.edu.backend.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/profile")
    public UserProfileDto getUserProfile() {
        return userService.getUserProfile();
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN')")
    @PostMapping("/password")
    public void changePassword(@RequestBody @Valid PasswordDto dto) {
        userService.changePassword(dto);
    }
}
