import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function StudentProfile() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [student, setStudent] = useState(null);
    const navigate = useNavigate();
    const { t } = useTranslation();

    useEffect(() => {
            const fetchStudent = async () => {
                try {
                    let res;

                    if (auth.accessToken) {
                        if (auth.role === "ROLE_STUDENT") {
                            res = await authFetch(`/api/students/profile`, {}, auth, setAuth);
                        } else if (auth.role === "ROLE_ADMIN") {
                            res = await authFetch(`/api/users/profile`, {}, auth, setAuth);
                        }
                    }
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
        }, [auth.accessToken]
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
            <h1 className="header">{t("label.general.profile")}</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul className="details-list">
                <li><strong>{t("label.student.fname")}:</strong> {student.firstName}</li>
                <li><strong>{t("label.student.lname")}:</strong> {student.lastName}</li>
                <li><strong>{t("label.student.email")}:</strong> {student.email}</li>
                {auth.accessToken && auth.role === "ROLE_STUDENT" && (
                    <li><strong>{t("label.student.bdate")}:</strong> {student.birthdate}</li>
                )}
            </ul>

            <Link className="btn btn-back" to="/home">{t("label.general.back")}</Link>
        </div>
    );
}

export default StudentProfile;