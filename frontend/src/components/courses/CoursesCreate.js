import React, {useContext, useState} from 'react';
import '../../styles/FormStyles.css';
import {validateCourse} from '../../validation/CourseValidation';
import {useNavigate, Link} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function CoursesCreate() {
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const {t} = useTranslation();

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

        const validationErrors = validateCourse(course, t);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage(t("error.validation"));
            return;
        }

        authFetch(
            '/api/courses',
            {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    ...course,
                    code: course.code.toUpperCase()
                })
            },
            auth, setAuth
        )
            .then(async (res) => {
                if (!res.ok) {
                    const data = await res.json();
                    if (data.fieldErrors) {
                        setErrors(data.fieldErrors);
                    }
                    if (data.message) {
                        setServerMessage(t(data.message));
                    }
                } else {
                    navigate('/courses');
                }
            })
            .catch(err => {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err)
            });
    };

    return (
        <div className="container">
            <h1>{t("label.course.create.header")}</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="code">{t("label.course.table.name")} </label>
                <input id="code" name="code" type="text" onChange={handleChange} required/>
                {errors.code && <span className="error">{errors.code}</span>}

                <label htmlFor="name">{t("label.course.table.code")} </label>
                <input id="name" name="name" type="text" onChange={handleChange} required/>
                {errors.name && <span className="error">{errors.name}</span>}

                <label htmlFor="credit">{t("label.course.table.credit")} </label>
                <input id="credit" name="credit" type="number" onChange={handleChange} required/>
                {errors.credit && <span className="error">{errors.credit}</span>}

                <label htmlFor="description">{t("label.course.description")} </label>
                <input id="description" name="description" type="text" onChange={handleChange} required/>
                {errors.description && <span className="error">{errors.description}</span>}

                <button className="btn btn-submit" type="submit">{t("label.general.create")}</button>
                <Link className="btn btn-cancel" to="/courses">{t("label.general.cancel")}</Link>
            </form>
        </div>
    );
}

export default CoursesCreate;