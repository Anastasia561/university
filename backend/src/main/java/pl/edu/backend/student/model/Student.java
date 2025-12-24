package pl.edu.backend.student.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.model.User;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends User {
    @Column(name = "birth_date")
    private LocalDate birthdate;
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}
