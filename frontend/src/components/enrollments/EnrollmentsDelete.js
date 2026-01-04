import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function EnrollmentsDelete() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const { t } = useTranslation();

    const [enrollment, setEnrollment] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await authFetch(`/api/enrollments/${id}`,
                {method: 'DELETE'}, auth, setAuth);

            if (!res.ok) {
                const data = await res.json();
                setServerMessage(data.message);
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

    useEffect(() => {
        const fetchEnrollment = async () => {
            try {
                const res = await authFetch(`/api/enrollments/${id}`, {}, auth, setAuth);
                const data = await res.json();

                if (!res.ok) {
                    setServerMessage(data.message);
                } else {
                    setEnrollment(data);
                }
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchEnrollment();
    }, [id, auth?.accessToken]);

    if (loading) return <p>{t("label.enrollment.loading")}</p>;

    return (
        <div className="confirm-box">
            <h2>{t("label.general.confirm")}</h2>

            {serverMessage ? (
                <div className="error general-error">{serverMessage}</div>
            ) : (
                <>
                    <p>{t("label.enrollment.delete.confirm")}</p>
                    <ul>
                        <li><strong>{t("label.course.table.code")}: </strong>{enrollment.courseCode}</li>
                        <li><strong>{t("label.student.email.student")}: </strong>{enrollment.studentEmail}</li>
                        <li><strong>{t("label.enrollment.date")}: </strong>{enrollment.enrollmentDate}</li>
                        <li><strong>{t("label.enrollment.grade")}: </strong>{enrollment.finalGrade}</li>
                    </ul>

                    <div>
                        <button onClick={handleDelete} className="btn btn-delete">
                            {t("label.general.delete")}</button>
                        <button onClick={() => navigate('/enrollments')}
                                className="btn btn-cancel">{t("label.general.cancel")}</button>
                    </div>
                </>
            )}
        </div>
    );
}

export default EnrollmentsDelete;