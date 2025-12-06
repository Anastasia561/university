package pl.edu.university.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @Column(unique = true)
    private String code;
    private Integer credit;
    private String description;
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<Enrollment>();
}
