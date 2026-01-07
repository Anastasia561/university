package pl.edu.backend.exception;

public class StudentAlreadyEnrolledException extends RuntimeException {
    public StudentAlreadyEnrolledException(String email, String code) {
        super("Student with email " + email + " is already enrolled for course with code " + code);
    }
}
