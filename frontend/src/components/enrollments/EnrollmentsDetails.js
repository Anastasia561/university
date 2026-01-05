import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function EnrollmentsDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [enrollment, setEnrollment] = useState(null);
    const navigate = useNavigate();
    const { t } = useTranslation();

    useEffect(() => {
        const fetchEnrollment = async () => {
            try {
                const res = await authFetch(`/api/enrollments/details/${id}`, {}, auth, setAuth);
                const data = await res.json();

                if (!res.ok) {
                    setServerMessage(t("auth.server.error"))
                    return;
                }

                setEnrollment(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            }
        };

        fetchEnrollment();
    }, [id, auth?.accessToken]);

    if (serverMessage) {
        return <div className="container">
            <div className="error general-error">{serverMessage}</div>
        </div>;
    }

    if (!enrollment) {
        return <p>{t("label.enrollment.loading")}</p>;
    }

    return (
        <div className="container">
            <h1>{t("label.enrollment.details")}</h1>

            <div className="info-container">
                <div className="info-box">
                    <h2 className="header">{t("label.student.info")}</h2>
                    <ul className="details-list">
                        <li><strong>{t("label.student.fname")}:</strong> {enrollment.student.firstName}</li>
                        <li><strong>{t("label.student.lname")}:</strong> {enrollment.student.lastName}</li>
                        <li><strong>{t("label.student.email")}:</strong> {enrollment.student.email}</li>
                    </ul>
                </div>

                <div className="info-box">
                    <h2 className="header">{t("label.course.info")}</h2>
                    <ul className="details-list">
                        <li><strong>{t("label.course.table.code")}:</strong> {enrollment.course.code}</li>
                        <li><strong>{t("label.course.table.name")}:</strong> {enrollment.course.name}</li>
                        <li><strong>{t("label.course.table.credit")}:</strong> {enrollment.course.credit}</li>
                        <li><strong>{t("label.course.description")}:</strong> {enrollment.course.description}</li>
                    </ul>
                </div>
            </div>

            <h2 className="header">{t("label.enrollment.info")}</h2>
            <table>
                <thead>
                <tr>
                    <th>{t("label.enrollment.full.date")}</th>
                    <th>{t("label.enrollment.grade")}</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{enrollment.enrollmentDate}</td>
                    <td>{enrollment.finalGrade != null ? enrollment.finalGrade : '-'}</td>
                </tr>
                </tbody>
            </table>

            <Link className="btn btn-back" to="/enrollments">{t("label.enrollment.back")}</Link>
        </div>
    );
}

export default EnrollmentsDetails;