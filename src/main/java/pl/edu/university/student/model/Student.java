package pl.edu.university.student.model;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.university.enrollment.model.Enrollment;
import pl.edu.university.user.model.User;

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
