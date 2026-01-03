import {Link, useNavigate} from "react-router-dom";
import React, {useContext, useState} from "react";
import AuthContext from "../../context/AuthProvider";
import {validateStudent} from "../../validation/StudentValidation";
import {authFetch} from "../auth/AuthFetch";
import {FaEye, FaEyeSlash} from "react-icons/fa";

function StudentsCreate() {
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);
    const [showPassword, setShowPassword] = useState(false);
    const [showRepeatPassword, setShowRepeatPassword] = useState(false);

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [student, setStudent] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        repeatPassword: '',
        birthdate: ''
    });

    const handleChange = (e) => {
        setStudent({
            ...student,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateStudent(student);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage("Validation error");
            return;
        }

        try {
            const res = await authFetch(
                '/api/students/register',
                {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(student)
                },
                auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (data.message) setServerMessage(data.message);
            } else {
                navigate('/login');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.error(err);
        }
    };

    return (
        <div className="container">
            <h1>Add Student</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="firstName">First Name: </label>
                <input id="firstName" name="firstName" type="text" onChange={handleChange} required/>
                {errors.firstName && <span className="error">{errors.firstName}</span>}

                <label htmlFor="lastName">Last Name: </label>
                <input id="lastName" name="lastName" type="text" onChange={handleChange} required/>
                {errors.lastName && <span className="error">{errors.lastName}</span>}

                <label htmlFor="email">Email: </label>
                <input id="email" name="email" type="email" onChange={handleChange} required/>
                {errors.email && <span className="error">{errors.email}</span>}

                <label>Password: </label>
                <div>
                    <div style={{position: "relative", width: "100%"}}>
                        <input
                            type={showPassword ? "text" : "password"}
                            name="password"
                            value={student.password || ""}
                            onChange={handleChange}
                            placeholder="Enter password"
                        />
                        <span onClick={() => setShowPassword(prev => !prev)} className="passwordIcon">
                    {showPassword ? <FaEyeSlash/> : <FaEye/>}
                </span>
                    </div>
                    {errors.password && <span className="error">{errors.password}</span>}
                </div>

                <label>Repeat password: </label>
                <div>
                    <div style={{position: "relative", width: "100%"}}>
                        <input
                            type={showRepeatPassword ? "text" : "password"}
                            name="repeatPassword"
                            value={student.repeatPassword || ""}
                            onChange={handleChange}
                            placeholder="Repeat password"
                        />
                        <span onClick={() => setShowRepeatPassword(prev => !prev)} className="passwordIcon">
                    {showRepeatPassword ? <FaEyeSlash/> : <FaEye/>}
                </span>
                    </div>
                    {errors.repeatPassword && <span className="error">{errors.repeatPassword}</span>}
                </div>

                <label htmlFor="birthdate">Birth Date:</label>
                <input id="birthdate" name="birthdate" type="date" onChange={handleChange} required/>
                {errors.birthdate && <span className="error">{errors.birthdate}</span>}

                <button className="btn btn-submit" type="submit">Create</button>
                <Link className="btn btn-cancel" to="/students">Cancel</Link>
            </form>
        </div>
    );
}

export default StudentsCreate;