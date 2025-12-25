import React, {useState} from 'react';
import '../../styles/FormStyles.css';
import {validateCourse} from '../../validation/CourseValidation';
import {useNavigate, Link} from 'react-router-dom';

function CoursesCreate() {
    const navigate = useNavigate();

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [course, setCourse] = useState({
        name: '',
        code: '',
        credit: '',
        description: ''
    });

    const handleChange = (e) => {
        setCourse({
            ...course,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const validationErrors = validateCourse(course);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage("Validation error");
            return;
        }

        fetch('/api/courses', {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(course)
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
                    navigate('/courses');
                }
            })
            .catch(err => console.error(err));
    };

    return (
        <div className="container">
            <h1>Add Course</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="code">Course Code: </label>
                <input id="code" name="code" type="text" onChange={handleChange} required/>
                {errors.code && <span className="error">{errors.code}</span>}

                <label htmlFor="name">Course Name: </label>
                <input id="name" name="name" type="text" onChange={handleChange} required/>
                {errors.name && <span className="error">{errors.name}</span>}

                <label htmlFor="credit">Credit: </label>
                <input id="credit" name="credit" type="number" onChange={handleChange} required/>
                {errors.credit && <span className="error">{errors.credit}</span>}

                <label htmlFor="description">Description: </label>
                <input id="description" name="description" type="text" onChange={handleChange} required/>
                {errors.description && <span className="error">{errors.description}</span>}

                <button className="btn btn-submit" type="submit">Create</button>
                <Link className="btn btn-cancel" to="/courses">Cancel</Link>
            </form>
        </div>
    );
}

export default CoursesCreate;