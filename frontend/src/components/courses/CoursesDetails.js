import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function CoursesDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate()
    const {t} = useTranslation();

    const [serverMessage, setServerMessage] = useState('');
    const [course, setCourse] = useState(null);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/courses/details/${id}`, {}, auth, setAuth);

                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                    return;
                }

                setCourse(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            }
        };
        fetchCourse();
    }, [id]);

    if (serverMessage) {
        return <div className="container">
            <div className="error general-error">{serverMessage}</div>
        </div>;
    }

    if (!course) {
        return <p>{t("label.course.loading")}</p>;
    }

    return (
        <div className="container">
            <h1 className="header">{t("label.course.details")}</h1>
            <h2 className="header">{t("label.course.info")}</h2>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul className="details-list">
                <li><strong>{t("label.course.table.code")}:</strong> {course.code}</li>
                <li><strong>{t("label.course.table.name")}:</strong> {course.name}</li>
                <li><strong>{t("label.course.table.credit")}:</strong> {course.credit}</li>
                <li><strong>{t("label.course.description")}:</strong> {course.description}</li>
            </ul>

            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                <h2 className="header">{t("label.course.students")}</h2>
            )}

            {auth.accessToken && auth.role === "ROLE_STUDENT" && (
                <h2 className="header">{t("label.nav.enrollments")}</h2>
            )}

            <table>
                <thead>
                <tr>
                    {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                        <th>{t("label.student.email.student")}</th>
                    )}
                    <th>{t("label.enrollment.date")}</th>
                    <th>{t("label.enrollment.grade")}</th>
                </tr>
                </thead>
                <tbody>
                {course.students.length === 0 ? (
                    <tr>
                        <td colSpan="3">{t("label.students.no")}</td>
                    </tr>
                ) : (
                    Array.isArray(course.students) && course.students.map((s, index) => (
                        <tr key={index}>
                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <td>{s.email}</td>
                            )}
                            <td>{s.enrollmentDate}</td>
                            <td>{s.finalGrade != null ? s.finalGrade : '-'}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>

            <Link className="btn btn-back" to="/courses">{t("label.course.back")}</Link>
        </div>
    );
}

export default CoursesDetails;