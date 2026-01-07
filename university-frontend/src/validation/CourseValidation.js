export const validateCourse = (course, t) => {
    const errors = {};

    if (course.name.trim() === "") {
        errors.name = t("validation.course.name.no");
    } else if (course.name.length < 3) {
        errors.name = t("validation.course.name.less");
    } else if (course.name.length > 30) {
        errors.name = t("validation.course.name.more");
    }

    if (course.code.trim() === "") {
        errors.code = t("validation.course.code.no");
    } else if (course.code.length < 2) {
        errors.code = t("validation.course.code.less");
    } else if (course.code.length > 5) {
        errors.code = t("validation.course.code.more");
    }

    if (course.description.trim() === "") {
        errors.description = t("validation.course.descr.no");
    } else if (course.description.length < 2) {
        errors.description = t("validation.course.descr.less");
    } else if (course.description.length > 100) {
        errors.description = t("validation.course.descr.more");
    }

    if (!course.credit) {
        errors.credit = t("validation.course.credit.no");
    } else if (isNaN(course.credit) || Number(course.credit) <= 0) {
        errors.credit = t("validation.course.credit.valid");
    }

    return errors;
};