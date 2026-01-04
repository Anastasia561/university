import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function StudentsDelete() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const { t } = useTranslation();

    const [student, setStudent] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();

        try {
            const res = await authFetch(
                `/api/students/${id}`,
                {method: 'DELETE'},
                auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                setServerMessage(data.message);
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

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await authFetch(`/api/students/details/${id}`, {}, auth, setAuth);
                const data = await res.json();

                if (!res.ok) {
                    setServerMessage(data.message);
                    return;
                }

                setStudent(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            } finally {
                setLoading(false);
            }
        };

        fetchStudent();
    }, [id, auth?.accessToken]);

    if (loading) return <p>{t("label.student.loading")}</p>;

    return (
        <div className="confirm-box">
            <h2>{t("label.general.confirm")}</h2>

            {serverMessage ? (
                <div className="error general-error">{serverMessage}</div>
            ) : (
                <>
                    <p>{t("label.student.delete.confirm")}</p>
                    <ul>
                        <li><strong>{t("label.student.fname")}: </strong>{student.firstName}</li>
                        <li><strong>{t("label.student.lname")}: </strong>{student.lastName}</li>
                        <li><strong>{t("label.student.email")}: </strong>{student.email}</li>
                        <li><strong>{t("label.student.bdate")}: </strong>{student.birthdate}</li>
                    </ul>

                    <div>
                        <button onClick={handleDelete} className="btn btn-delete">
                            {t("label.general.delete")}</button>
                        <button onClick={() => navigate('/students')}
                                className="btn btn-cancel">{t("label.general.cancel")}</button>
                    </div>
                </>
            )}
        </div>
    );
}

export default StudentsDelete;