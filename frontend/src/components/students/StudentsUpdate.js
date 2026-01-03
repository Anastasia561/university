import React, {useState, useEffect, useContext} from 'react';
import '../../styles/FormStyles.css';
import {validateStudent} from '../../validation/StudentValidation';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function StudentsUpdate() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);

    const [student, setStudent] = useState(null);
    const [errors, setErrors] = useState({});
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await authFetch(`/api/students/details/${id}`, {}, auth, setAuth);
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
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
    }, [id, auth.accessToken]);

    const handleChange = (e) => {
        const {name, value} = e.target;
        setStudent(prev => ({...prev, [name]: value}));
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateStudent(student);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage("Validation error");
            return;
        }

        try {
            const res = await authFetch(`/api/students/${id}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        ...student
                    })
                }, auth, setAuth
            );

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (data.message) setServerMessage(data.message);
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

    if (loading) return <p>Loading student data...</p>;

    return (
        <div className="container">
            <h1>Update Student</h1>
            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="firstName">First Name:</label>
                <input id="firstName" name="firstName" type="text"
                       value={student.firstName || ''} onChange={handleChange}/>
                {errors.firstName && <span className="error">{errors.firstName}</span>}

                <label htmlFor="lastName">Last Name:</label>
                <input id="lastName" name="lastName" type="text"
                       value={student.lastName || ''} onChange={handleChange}/>
                {errors.lastName && <span className="error">{errors.lastName}</span>}

                <label htmlFor="email">Email:</label>
                <input id="email" name="email" type="email"
                       value={student.email || ''} onChange={handleChange}/>
                {errors.email && <span className="error">{errors.email}</span>}

                <label htmlFor="birthdate">Birth Date:</label>
                <input id="birthdate" name="birthdate" type="date"
                       value={student.birthdate || ''} onChange={handleChange}/>
                {errors.birthdate && <span className="error">{errors.birthdate}</span>}

                <button className="btn btn-submit" type="submit">Update</button>
                <Link className="btn btn-cancel" to="/students">Cancel</Link>
            </form>
        </div>
    );
}

export default StudentsUpdate;