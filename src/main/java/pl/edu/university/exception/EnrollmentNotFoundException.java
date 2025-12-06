package pl.edu.university.exception;

public class EnrollmentNotFoundException extends RuntimeException {
    public EnrollmentNotFoundException(Integer id) {
        super("Enrollment with id " + id + " not found");
    }
}
