import React, {useContext, useState} from 'react';
import '../../styles/FormStyles.css';
import {validateStudent} from '../../validation/StudentValidation';
import {useNavigate, Link} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function StudentsCreate() {
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const { t } = useTranslation();

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

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateStudent(student, t);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage(t("error.validation"));
            return;
        }

        try {
            const res = await authFetch(
                '/api/students',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(student)
                },
                auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (res.status === 400) {
                    setServerMessage(t("error.validation"));
                }else{
                    setServerMessage(t("auth.server.error"));
                }
            } else {
                navigate('/students');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.error(err);
        }
    };

    return (
        <div className="container">
            <h1>{t("label.student.create")}</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="firstName">{t("label.student.fname")}: </label>
                <input id="firstName" name="firstName" type="text" onChange={handleChange} required/>
                {errors.firstName && <span className="error">{errors.firstName}</span>}

                <label htmlFor="lastName">{t("label.student.lname")}: </label>
                <input id="lastName" name="lastName" type="text" onChange={handleChange} required/>
                {errors.lastName && <span className="error">{errors.lastName}</span>}

                <label htmlFor="email">{t("label.student.email")}: </label>
                <input id="email" name="email" type="email" onChange={handleChange} required/>
                {errors.email && <span className="error">{errors.email}</span>}

                <label htmlFor="birthdate">{t("label.student.bdate")}:</label>
                <input id="birthdate" name="birthdate" type="date" onChange={handleChange} required/>
                {errors.birthdate && <span className="error">{errors.birthdate}</span>}

                <button className="btn btn-submit" type="submit">{t("label.general.create")}</button>
                <Link className="btn btn-cancel" to="/students">{t("label.general.cancel")}</Link>
            </form>
        </div>
    );
}

export default StudentsCreate;