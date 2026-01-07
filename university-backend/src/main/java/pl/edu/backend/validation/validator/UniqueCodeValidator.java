package pl.edu.backend.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import pl.edu.backend.course.repository.CourseRepository;
import pl.edu.backend.validation.annotation.UniqueCode;

@Component
public class UniqueCodeValidator implements ConstraintValidator<UniqueCode, String> {
    private final CourseRepository courseRepository;

    public UniqueCodeValidator(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null || code.isBlank()) {
            return true;
        }
        return !courseRepository.existsByCode(code);
    }
}
