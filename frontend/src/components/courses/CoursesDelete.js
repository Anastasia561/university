import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function CoursesDelete() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const {t} = useTranslation();

    const [course, setCourse] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await authFetch(`/api/courses/${id}`, {
                    method: 'DELETE'
                },
                auth, setAuth
            );
            if (!res.ok) {
                const data = await res.json();
                setServerMessage(t("auth.server.error"));
            } else {
                navigate('/courses');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.log(err);
        }
    };

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/courses/${id}`, {
                        headers: {
                            'Content-Type': 'application/json'
                        },
                    },
                    auth, setAuth
                );
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

    if (loading) return <p>{t("label.courses.loading")}</p>;

    return (
        <div className="confirm-box">
            <h2>{t("label.general.confirm")}</h2>

            {serverMessage ? (
                <div className="error general-error">{serverMessage}</div>
            ) : (
                <>
                    <p>{t("label.course.delete.confirm")}</p>
                    <ul>
                        <li><strong>{t("label.course.table.name")}: </strong>{course.name}</li>
                        <li><strong>{t("label.course.table.code")}: </strong>{course.code}</li>
                        <li><strong>{t("label.course.table.credit")}: </strong>{course.credit}</li>
                    </ul>

                    <div>
                        <button onClick={handleDelete} className="btn btn-delete">{t("label.general.delete")}</button>
                        <button onClick={() => navigate('/courses')}
                                className="btn btn-cancel">{t("label.general.cancel")}</button>
                    </div>
                </>
            )}
        </div>
    );
}

export default CoursesDelete;