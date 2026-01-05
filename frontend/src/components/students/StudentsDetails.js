import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function StudentsDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [student, setStudent] = useState(null);
    const navigate = useNavigate();
    const { t } = useTranslation();

    useEffect(() => {
            const fetchStudent = async () => {
                try {
                    const res = await authFetch(`/api/students/details/${id}`, {}, auth, setAuth);

                    const data = await res.json();
                    if (!res.ok) {
                        setServerMessage(t("auth.server.error"))
                        return;
                    }

                    setStudent(data);
                } catch (err) {
                    if (err.message === 'Session expired') {
                        navigate('/login');
                    }
                    console.error(err);
                }
            };
            fetchStudent();
        }, [id, auth.accessToken]
    );

    if (serverMessage) {
        return <div className="container">
            <div className="error general-error">{serverMessage}</div>
        </div>;
    }

    if (!student) {
        return <p>{t("label.student.loading")}</p>;
    }

    return (
        <div className="container">
            <h1 className="header">{t("label.student.details")}</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul className="details-list">
                <li><strong>{t("label.student.fname")}:</strong> {student.firstName}</li>
                <li><strong>{t("label.student.lname")}:</strong> {student.lastName}</li>
                <li><strong>{t("label.student.email")}:</strong> {student.email}</li>
                <li><strong>{t("label.student.bdate")}:</strong> {student.birthdate}</li>
            </ul>

            <h2 className="header">{t("label.nav.enrollments")}</h2>

            <table>
                <thead>
                <tr>
                    <th>{t("label.course.table.code")}</th>
                    <th>{t("label.enrollment.date")}</th>
                    <th>{t("label.enrollment.grade")}</th>
                </tr>
                </thead>
                <tbody>
                {student.enrollments.length === 0 ? (
                    <tr>
                        <td colSpan="3">{t("label.enrollment.no")}</td>
                    </tr>
                ) : (
                    Array.isArray(student.enrollments) && student.enrollments.map((e, index) => (
                        <tr key={index}>
                            <td>{e.courseCode}</td>
                            <td>{e.enrollmentDate}</td>
                            <td>{e.finalGrade != null ? e.finalGrade : '-'}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>

            <Link className="btn btn-back" to="/students">{t("label.student.back")}</Link>
        </div>
    );
}

export default StudentsDetails;