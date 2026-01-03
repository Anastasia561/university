import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function StudentsDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [student, setStudent] = useState(null);
    const navigate = useNavigate();

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
        return <p>Loading student details...</p>;
    }

    return (
        <div className="container">
            <h1 className="header">Student Details</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul className="details-list">
                <li><strong>First Name:</strong> {student.firstName}</li>
                <li><strong>Last Name:</strong> {student.lastName}</li>
                <li><strong>Email:</strong> {student.email}</li>
                <li><strong>Birth Date:</strong> {student.birthdate}</li>
            </ul>

            <h2 className="header">Enrollments</h2>

            <table>
                <thead>
                <tr>
                    <th>Course Code</th>
                    <th>Due Date</th>
                    <th>Final Grade</th>
                </tr>
                </thead>
                <tbody>
                {student.enrollments.length === 0 ? (
                    <tr>
                        <td colSpan="3">No enrollments</td>
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

            <Link className="btn btn-back" to="/students">Back to Students List</Link>
        </div>
    );
}

export default StudentsDetails;