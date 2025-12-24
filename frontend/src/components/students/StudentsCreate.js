import React, {useState} from 'react';
import '../../styles/FormStyles.css';
import {useNavigate, Link} from 'react-router-dom';

function StudentsCreate() {
    const navigate = useNavigate();

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [student, setStudent] = useState({
        firstName: '',
        lastName: '',
        email: '',
        birthdate: ''
    });

    const validate = () => {
        const newErrors = {};

        if (student.firstName.trim() === "") {
            newErrors.firstName = "First name is required";
        } else if (student.firstName.length < 3) {
            newErrors.firstName = "First name can not be less then 3 characters";
        } else if (student.firstName.length > 30) {
            newErrors.firstName = "First name can not be more then 30 characters";
        }

        if (student.lastName.trim() === "") {
            newErrors.lastName = "Last name is required";
        } else if (student.lastName.length < 3) {
            newErrors.lastName = "Last name can not be less then 3 characters";
        } else if (student.lastName.length > 30) {
            newErrors.lastName = "Last name can not be more then 30 characters";
        }

        let now = new Date();
        let date = new Date(student.birthdate);
        if (!student.birthdate) {
            newErrors.birthdate = "Birthdate field is required";
        } else if (date > now) {
            newErrors.birthdate = "Birthdate can not be in the future";
        } else if (now.getFullYear() - date.getFullYear() < 18) {
            newErrors.birthdate = "Minimum age is required : 18 years";
        }

        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        if (student.email.trim() === "") {
            newErrors.email = "Email is required";
        } else if (!emailPattern.test(student.email.trim())) {
            newErrors.email = "Email is not valid";
        }

        setErrors(newErrors);

        return Object.keys(newErrors).length === 0;
    };

    const handleChange = (e) => {
        setStudent({
            ...student,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!validate()) {
            setServerMessage("Validation error");
            return;
        }

        fetch('/api/students', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(student)
        })
            .then(async (res) => {
                if (!res.ok) {
                    const data = await res.json();
                    if (data.fieldErrors) {
                        setErrors(data.fieldErrors);
                    }
                    if (data.message) {
                        setServerMessage(data.message);
                    }
                } else {
                    navigate('/students');
                }
            })
            .catch(err => console.error(err));
    };

    return (
        <div className="container">
            <h1>Add Student</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="firstName">First Name: </label>
                <input id="firstName" name="firstName" type="text" onChange={handleChange} required/>
                {errors.firstName && <span className="error">{errors.firstName}</span>}

                <label htmlFor="lastName">Last Name: </label>
                <input id="lastName" name="lastName" type="text" onChange={handleChange} required/>
                {errors.lastName && <span className="error">{errors.lastName}</span>}

                <label htmlFor="email">Email: </label>
                <input id="email" name="email" type="email" onChange={handleChange} required/>
                {errors.email && <span className="error">{errors.email}</span>}

                <label htmlFor="birthdate">Birth Date:</label>
                <input id="birthdate" name="birthdate" type="date" onChange={handleChange} required/>
                {errors.birthdate && <span className="error">{errors.birthdate}</span>}

                <button className="btn btn-submit" type="submit">Create</button>
                <Link className="btn btn-cancel" to="/students">Cancel</Link>
            </form>
        </div>
    );
}

export default StudentsCreate;