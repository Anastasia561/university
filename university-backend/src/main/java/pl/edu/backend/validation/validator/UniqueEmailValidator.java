package pl.edu.backend.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import pl.edu.backend.student.repository.StudentRepository;
import pl.edu.backend.validation.annotation.UniqueEmail;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final StudentRepository studentRepository;

    public UniqueEmailValidator(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        return !studentRepository.existsByEmail(email);
    }
}