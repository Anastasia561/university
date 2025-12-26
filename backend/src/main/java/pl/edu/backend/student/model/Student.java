package pl.edu.backend.student.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pl.edu.backend.enrollment.model.Enrollment;
import pl.edu.backend.user.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends User {
    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid = UUID.randomUUID();
    @Column(name = "birth_date")
    private LocalDate birthdate;
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}
