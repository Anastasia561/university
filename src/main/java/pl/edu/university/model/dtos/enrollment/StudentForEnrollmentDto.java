package pl.edu.university.model.dtos.enrollment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentForEnrollmentDto {
    private String firstName;
    private String lastName;
    private String email;
}
