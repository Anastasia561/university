import React, {useState, useEffect, useContext} from 'react';
import '../../styles/FormStyles.css';
import {useNavigate, useParams, Link} from 'react-router-dom';
import {validateEnrollment} from "../../validation/EnrollmentValidation";
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function EnrollmentsUpdate() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const { t } = useTranslation();

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [enrollment, setEnrollment] = useState({
        courseCode: '',
        studentEmail: '',
        enrollmentDate: '',
        finalGrade: ''
    });

    useEffect(() => {
        authFetch('/api/students/all', {}, auth, setAuth)
            .then(res => res.json())
            .then(data => setStudents(data))
            .catch(err => console.error(err));

        authFetch('/api/courses/all', {}, auth, setAuth)
            .then(res => res.json())
            .then(data => setCourses(data))
            .catch(err => console.error(err));
    }, [auth.accessToken]);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/enrollments/${id}`, {}, auth, setAuth);
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                }
                setEnrollment(data);
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
    }, [id, auth.accessToken]);

    const handleChange = (e) => {
        setEnrollment({
            ...enrollment,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateEnrollment(enrollment);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage(t("error.validation"));
            return;
        }

        try {
            const res = await authFetch(
                `/api/enrollments/${id}`,
                {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(enrollment)
                }, auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (data.message) setServerMessage(data.message);
            } else {
                navigate('/enrollments');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.error(err);
        }
    };

    if (loading) return <p>{t("label.enrollment.loading")}</p>;

    return (
        <div className="container">
            <h1>{t("label.enrollment.update")}</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="studentEmail">{t("label.student.email.student")}:</label>
                <select id="studentEmail" name="studentEmail" value={enrollment.studentEmail}
                        onChange={handleChange} required>
                    <option value="">{t("label.student.select")}</option>
                    {Array.isArray(students) && students.map(s => (
                        <option key={s.id} value={s.email}>{s.email}</option>
                    ))}
                </select>
                {errors.studentEmail && <span className="error">{errors.studentEmail}</span>}

                <label htmlFor="courseCode">{t("label.course.table.code")}:</label>
                <select id="courseCode" name="courseCode" value={enrollment.courseCode}
                        onChange={handleChange} required>
                    <option value="">{t("label.course.select")}</option>
                    {Array.isArray(courses) && courses.map(c => (
                        <option key={c.id} value={c.code}>{c.code}</option>
                    ))}
                </select>
                {errors.courseCode && <span className="error">{errors.courseCode}</span>}

                <label htmlFor="enrollmentDate">{t("label.enrollment.date")}:</label>
                <input type="date" id="enrollmentDate" name="enrollmentDate"
                       value={enrollment.enrollmentDate} onChange={handleChange} required/>
                {errors.enrollmentDate && <span className="error">{errors.enrollmentDate}</span>}

                <label htmlFor="finalGrade">{t("label.enrollment.grade")}:</label>
                <select id="finalGrade" name="finalGrade" value={enrollment.finalGrade || ''}
                        onChange={handleChange}>
                    <option value="">Select Grade</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="3.5">3.5</option>
                    <option value="4">4</option>
                    <option value="4.5">4.5</option>
                    <option value="5">5</option>
                </select>

                <button type="submit" className="btn btn-submit">Update</button>
                <Link className="btn btn-cancel" to="/enrollments">Cancel</Link>
            </form>
        </div>
    );
}

export default EnrollmentsUpdate;
