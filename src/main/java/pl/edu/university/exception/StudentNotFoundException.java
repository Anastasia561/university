package pl.edu.university.exception;

public class StudentNotFoundException extends RuntimeException {
    public StudentNotFoundException(String email) {
        super("Student with email " + email + " not found");
    }

    public StudentNotFoundException(Integer id) {
        super("Student with id " + id + " not found");
    }
}
