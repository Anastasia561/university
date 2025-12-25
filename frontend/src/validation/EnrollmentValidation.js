export const validateEnrollment = (enrollment) => {
    const errors = {};

    if (!enrollment.studentEmail) errors.studentEmail = "Student is required";

    if (!enrollment.courseCode) errors.courseCode = "Course is required";

    if (!enrollment.enrollmentDate) {
        errors.enrollmentDate = "Enrollment date is required";
    } else {
        const selectedDate = new Date(enrollment.enrollmentDate);
        const today = new Date();
        if (selectedDate < today) {
            errors.enrollmentDate = "Enrollment date cannot be in the past";
        }
    }

    return errors;
};