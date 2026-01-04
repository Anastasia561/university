import React, {useState, useEffect, useContext} from 'react';
import '../../styles/FormStyles.css';
import {validateCourse} from '../../validation/CourseValidation';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function CoursesUpdate() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const {t} = useTranslation();

    const [course, setCourse] = useState(null);
    const [errors, setErrors] = useState({});
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/courses/details/${id}`, {}, auth, setAuth);
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                }
                setCourse(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchCourse();
    }, [id]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setCourse(prev => ({...prev, [name]: value}));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateCourse(course);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage(t("error.validation"));
            return;
        }

        try {
            const res = await authFetch(`/api/courses/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        ...course,
                        code: course.code.toUpperCase()
                    })
                },
                auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (data.message) setServerMessage(data.message);
            } else {
                navigate('/courses');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.error(err);
        }
    };

    if (loading) return <p>{t("label.course.loading")}</p>;

    return (
        <div className="container">
            <h1>{t("label.course.update")}</h1>
            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="code">{t("label.course.table.code")}: </label>
                <input id="code" name="code" type="text"
                       value={course.code || ''} onChange={handleChange}/>
                {errors.code && <span className="error">{errors.code}</span>}

                <label htmlFor="name">{t("label.course.table.name")}: </label>
                <input id="name" name="name" type="text"
                       value={course.name || ''} onChange={handleChange}/>
                {errors.name && <span className="error">{errors.name}</span>}

                <label htmlFor="credit">{t("label.course.table.credit")}: </label>
                <input id="credit" name="credit" type="number"
                       value={course.credit || ''} onChange={handleChange}/>
                {errors.credit && <span className="error">{errors.credit}</span>}

                <label htmlFor="description">{t("label.course.description")}: </label>
                <input id="description" name="description" type="text"
                       value={course.description || ''} onChange={handleChange}/>
                {errors.description && <span className="error">{errors.description}</span>}

                <button className="btn btn-submit" type="submit">{t("label.general.update")}</button>
                <Link className="btn btn-cancel" to="/courses">{t("label.general.cancel")}</Link>
            </form>
        </div>
    );
}

export default CoursesUpdate;