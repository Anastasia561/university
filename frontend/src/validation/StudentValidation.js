export const validateStudent = (student) => {
        const errors = {};

        if (student.firstName.trim() === "") {
            errors.firstName = "First name is required";
        } else if (student.firstName.length < 3) {
            errors.firstName = "First name can not be less then 3 characters";
        } else if (student.firstName.length > 30) {
            errors.firstName = "First name can not be more then 30 characters";
        }

        if (student.lastName.trim() === "") {
            errors.lastName = "Last name is required";
        } else if (student.lastName.length < 3) {
            errors.lastName = "Last name can not be less then 3 characters";
        } else if (student.lastName.length > 30) {
            errors.lastName = "Last name can not be more then 30 characters";
        }

        let now = new Date();
        let date = new Date(student.birthdate);
        if (!student.birthdate) {
            errors.birthdate = "Birthdate field is required";
        } else if (date > now) {
            errors.birthdate = "Birthdate can not be in the future";
        } else if (now.getFullYear() - date.getFullYear() < 18) {
            errors.birthdate = "Minimum age is required : 18 years";
        }

        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (student.email.trim() === "") {
            errors.email = "Email is required";
        } else if (!emailPattern.test(student.email.trim())) {
            errors.email = "Email is not valid";
        }

        if ("password" in student) {
            if (!student.password || student.password.trim() === "") {
                errors.password = "Password is required";
            } else if (student.password.length < 8) {
                errors.password = "Password must be at least 8 characters";
            } else if (!/\d/.test(student.password)) {
                errors.password = "Password must contain at least one digit";
            } else if (!/[!@#$%^&*(),.?":{}|<>]/.test(student.password)) {
                errors.password = "Password must contain at least one special character";
            }
        }

        if ("repeatPassword" in student) {
            if (!student.repeatPassword || student.repeatPassword.trim() === "") {
                errors.repeatPassword = "Repeat password is required";
            } else if (student.repeatPassword !== student.password) {
                errors.repeatPassword = "Passwords do not match";
            }
        }


        if (student.email.trim() === "") {
            errors.email = "Email is required";
        } else if (!emailPattern.test(student.email.trim())) {
            errors.email = "Email is not valid";
        }

        return errors;
    }
;
