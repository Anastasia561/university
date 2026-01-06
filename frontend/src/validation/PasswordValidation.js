export const validatePassword = (pass, rpass, t) => {
    const errors = {};

    if (!pass || pass.trim() === "") {
        errors.password = t("validation.student.pass.no");
    } else if (pass.length < 8) {
        errors.password = t("validation.student.pass.size");
    } else if (!/\d/.test(pass)) {
        errors.password = t("validation.student.pass.digit");
    } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(pass)) {
        errors.password = t("validation.student.pass.char");
    }


    if (!rpass || rpass.trim() === "") {
        errors.repeatPassword = t("validation.student.rpass.no");
    } else if (pass !== rpass) {
        errors.repeatPassword = t("validation.student.rpass.match");
    }

    return errors;
};