import React, {useState} from 'react';
import '../../styles/FormStyles.css';
import {validateStudent} from '../../validation/StudentValidation';
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

    const handleChange = (e) => {
        setStudent({
            ...student,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const validationErrors = validateStudent(student);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
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