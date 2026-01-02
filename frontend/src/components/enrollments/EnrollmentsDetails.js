import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function EnrollmentsDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [enrollment, setEnrollment] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEnrollment = async () => {
            try {
                const res = await authFetch(`/api/enrollments/details/${id}`, {}, auth, setAuth);
                const data = await res.json();

                if (!res.ok) {
                    setServerMessage(data.message);
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
        return <p>Loading enrollment details...</p>;
    }

    return (
        <div className="container">
            <h1>Enrollment Details</h1>

            <div className="info-container">
                <div className="info-box">
                    <h2 className="header">Student Information</h2>
                    <ul className="details-list">
                        <li><strong>First Name:</strong> {enrollment.student.firstName}</li>
                        <li><strong>Last Name:</strong> {enrollment.student.lastName}</li>
                        <li><strong>Email:</strong> {enrollment.student.email}</li>
                    </ul>
                </div>

                <div className="info-box">
                    <h2 className="header">Course Information</h2>
                    <ul className="details-list">
                        <li><strong>Course Code:</strong> {enrollment.course.code}</li>
                        <li><strong>Course Name:</strong> {enrollment.course.name}</li>
                        <li><strong>Credits:</strong> {enrollment.course.credit}</li>
                        <li><strong>Description:</strong> {enrollment.course.description}</li>
                    </ul>
                </div>
            </div>

            <h2 className="header">Enrollment Information</h2>
            <table>
                <thead>
                <tr>
                    <th>Enrollment Due Date</th>
                    <th>Final Grade</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{enrollment.enrollmentDate}</td>
                    <td>{enrollment.finalGrade != null ? enrollment.finalGrade : '-'}</td>
                </tr>
                </tbody>
            </table>

            <Link className="btn btn-back" to="/enrollments">Back to Enrollments List</Link>
        </div>
    );
}

export default EnrollmentsDetails;