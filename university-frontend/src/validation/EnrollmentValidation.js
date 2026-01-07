export const validateEnrollment = (enrollment, t) => {
    const errors = {};

    if (!enrollment.studentEmail) errors.studentEmail = t("validation.enrollment.student");

    if (!enrollment.courseCode) errors.courseCode = t("validation.enrollment.course")

    if (!enrollment.enrollmentDate) {
        errors.enrollmentDate = t("validation.enrollment.date.no")
    } else {
        const selectedDate = new Date(enrollment.enrollmentDate);
        const today = new Date();
        if (selectedDate < today) {
            errors.enrollmentDate = t("validation.enrollment.date.past")
        }
    }

    return errors;
};