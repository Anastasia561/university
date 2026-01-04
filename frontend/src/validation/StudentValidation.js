export const validateStudent = (student, t) => {
        const errors = {};

        if (student.firstName.trim() === "") {
            errors.firstName = t("validation.student.fname.no");
        } else if (student.firstName.length < 3) {
            errors.firstName = t("validation.student.fname.less");
        } else if (student.firstName.length > 30) {
            errors.firstName = t("validation.student.fname.more");
        }

        if (student.lastName.trim() === "") {
            errors.lastName = t("validation.student.lname.no");
        } else if (student.lastName.length < 3) {
            errors.lastName = t("validation.student.lname.less");
        } else if (student.lastName.length > 30) {
            errors.lastName = t("validation.student.lname.more");
        }

        let now = new Date();
        let date = new Date(student.birthdate);
        if (!student.birthdate) {
            errors.birthdate = t("validation.student.bdate.no");
        } else if (date > now) {
            errors.birthdate = t("validation.student.bdate.future");
        } else if (now.getFullYear() - date.getFullYear() < 18) {
            errors.birthdate = t("validation.student.bdate.min");
        }

        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (student.email.trim() === "") {
            errors.email = t("validation.student.email.no");
        } else if (!emailPattern.test(student.email.trim())) {
            errors.email = t("validation.student.email.valid");
        }

        if ("password" in student) {
            if (!student.password || student.password.trim() === "") {
                errors.password = t("validation.student.pass.no");
            } else if (student.password.length < 8) {
                errors.password = t("validation.student.pass.size");
            } else if (!/\d/.test(student.password)) {
                errors.password = t("validation.student.pass.digit");
            } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(student.password)) {
                errors.password = t("validation.student.pass.char");
            }
        }

        if ("repeatPassword" in student) {
            if (!student.repeatPassword || student.repeatPassword.trim() === "") {
                errors.repeatPassword = t("validation.student.rpass.no");
            } else if (student.repeatPassword !== student.password) {
                errors.repeatPassword = t("validation.student.rpass.match");
            }
        }

        return errors;
    }
;
