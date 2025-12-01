package pl.edu.university.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(int id) {
        super("Course with id " + id + " not found");
    }
}
