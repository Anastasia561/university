package pl.edu.university.enrollment.model;

import jakarta.persistence.*;
import lombok.*;
import pl.edu.university.course.model.Course;
import pl.edu.university.student.model.Student;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "enrollment")
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;
    @Column(name = "final_grade")
    private Double finalGrade;
    @Column(name = "enrollment_date")
    private LocalDate date;
}
