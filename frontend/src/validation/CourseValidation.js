export const validateCourse = (course) => {
    const errors = {};

    if (course.name.trim() === "") {
        errors.name = "Course name is required";
    } else if (course.name.length < 3) {
        errors.name = "Course name can not be less then 3 characters";
    } else if (course.name.length > 30) {
        errors.name = "Course name can not be more then 50 characters";
    }

    if (course.code.trim() === "") {
        errors.code = "Course code is required";
    } else if (course.code.length < 2) {
        errors.code = "Course code can not be less then 2 characters";
    } else if (course.code.length > 5) {
        errors.code = "Course code can not be more then 5 characters";
    }

    if (course.description.trim() === "") {
        errors.description = "Course description is required";
    } else if (course.description.length < 2) {
        errors.description = "Course description can not be less then 2 characters";
    } else if (course.description.length > 100) {
        errors.description = "Course description can not be more then 100 characters";
    }

    if (!course.credit) {
        errors.credit = "Credits are required";
    } else if (isNaN(course.credit) || Number(course.credit) <= 0) {
        errors.credit = "Credits must be a number greater than 0";
    }

    return errors;
};