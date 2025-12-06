package pl.edu.university.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "student")
public class Student extends User {
    @Column(name = "birth_date")
    private LocalDate birthdate;
    private String number;
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}
