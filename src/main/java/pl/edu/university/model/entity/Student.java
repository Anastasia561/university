package pl.edu.university.model.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
@Entity
@Table(name = "student")
public class Student extends User {
    @Column(name = "birth_date")
    private LocalDate birthdate;
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments;
}
