import '../../styles/DetailedViewStyles.css';
import '../../styles/PasswordUpdateDialogStyles.css'
import React, {useContext, useEffect, useRef, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";
import {validatePassword} from "../../validation/PasswordValidation";
import {FaEye, FaEyeSlash} from "react-icons/fa";

function StudentProfile() {
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [student, setStudent] = useState(null);
    const [errors, setErrors] = useState({});
    const navigate = useNavigate();
    const {t} = useTranslation();

    const [password, setPassword] = useState('');
    const [repeatPassword, setRepeatPassword] = useState('');
    const [passwordError, setPasswordError] = useState('');

    const [showPassword, setShowPassword] = useState(false);
    const [showRepeatPassword, setShowRepeatPassword] = useState(false);

    const passwordDialogRef = useRef(null);

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

            <div>
                <Link className="btn btn-back" to="/home">{t("label.general.back")}</Link>
                <button
                    className="btn btn-cancel"
                    onClick={() => passwordDialogRef.current.showModal()}>
                    {t("label.general.password.update")}
                </button>
            </div>

            <dialog ref={passwordDialogRef} className="password-dialog">
                <h2>{t("label.general.password.update")}</h2>

                {passwordError && <div className="error">{passwordError}</div>}

                <div className="form-group">
                    <label>{t("label.student.password")}: </label>
                    <div>
                        <div style={{position: "relative", width: "100%"}}>
                            <input
                                type={showPassword ? "text" : "password"}
                                name="password"
                                value={password || ""}
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder={t("label.student.password.placeholder")}
                            />
                            <span onClick={() => setShowPassword(prev => !prev)} className="passwordIcon">
                    {showPassword ? <FaEyeSlash/> : <FaEye/>}
                </span>
                        </div>
                        {errors.password && <span className="error">{errors.password}</span>}
                    </div>
                </div>

                <div className="form-group">
                    <label>{t("label.student.rpass")}: </label>
                    <div>
                        <div style={{position: "relative", width: "100%"}}>
                            <input
                                type={showRepeatPassword ? "text" : "password"}
                                name="repeatPassword"
                                value={repeatPassword || ""}
                                onChange={(e) => setRepeatPassword(e.target.value)}
                                placeholder={t("label.student.rpass")}
                            />
                            <span onClick={() => setShowRepeatPassword(prev => !prev)} className="passwordIcon">
                    {showRepeatPassword ? <FaEyeSlash/> : <FaEye/>}
                </span>
                        </div>
                        {errors.repeatPassword && <span className="error">{errors.repeatPassword}</span>}
                    </div>
                </div>

                <div className="modal-buttons">
                    <button
                        className="btn btn-back"
                        onClick={async () => {
                            const validationErrors = validatePassword(password, repeatPassword, t);
                            setErrors(validationErrors);

                            if (Object.keys(validationErrors).length > 0) {
                                setPasswordError(t("error.validation"));
                                return;
                            }
                            setPasswordError('');

                            try {
                                const res = await authFetch('/api/users/password', {
                                    method: 'POST',
                                    headers: {'Content-Type': 'application/json'},
                                    body: JSON.stringify({password, repeatPassword})
                                }, auth, setAuth);

                                if (!res.ok) {
                                    setPasswordError(t("auth.server.error"));
                                } else {
                                    passwordDialogRef.current.close();
                                    setPassword('');
                                    setRepeatPassword('');
                                }
                            } catch (err) {
                                console.error(err);
                            }
                        }}
                    >
                        {t("label.general.update")}
                    </button>

                    <button
                        className="btn btn-cancel"
                        onClick={() => passwordDialogRef.current.close()}
                    >
                        {t("label.general.cancel")}
                    </button>
                </div>
            </dialog>

        </div>
    );
}

export default StudentProfile;