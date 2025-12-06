package pl.edu.university.model.dtos.student;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentPreviewDto {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
}
