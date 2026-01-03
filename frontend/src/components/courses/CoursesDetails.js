import '../../styles/DetailedViewStyles.css';
import React, {useContext, useEffect, useState} from 'react';
import {Link, useNavigate, useParams} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function CoursesDetails() {
    const {id} = useParams();
    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate()

    const [serverMessage, setServerMessage] = useState('');
    const [course, setCourse] = useState(null);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/courses/details/${id}`, {}, auth, setAuth);

                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                    return;
                }

                setCourse(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            }
        };
        fetchCourse();
    }, [id]);

    if (serverMessage) {
        return <div className="container">
            <div className="error general-error">{serverMessage}</div>
        </div>;
    }

    if (!course) {
        return <p>Loading course details...</p>;
    }

    return (
        <div className="container">
            <h1 className="header">Course Details</h1>
            <h2 className="header">Course Information</h2>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul className="details-list">
                <li><strong>Course Code:</strong> {course.code}</li>
                <li><strong>Course Name:</strong> {course.name}</li>
                <li><strong>Credits:</strong> {course.credit}</li>
                <li><strong>Description:</strong> {course.description}</li>
            </ul>

            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                <h2 className="header">Enrolled Students</h2>
            )}

            {auth.accessToken && auth.role === "ROLE_STUDENT" && (
                <h2 className="header">Enrollments</h2>
            )}

            <table>
                <thead>
                <tr>
                    {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                        <th>Student Email</th>
                    )}
                    <th>Due Date</th>
                    <th>Final Grade</th>
                </tr>
                </thead>
                <tbody>
                {course.students.length === 0 ? (
                    <tr>
                        <td colSpan="3">No students</td>
                    </tr>
                ) : (
                    Array.isArray(course.students) && course.students.map((s, index) => (
                        <tr key={index}>
                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <td>{s.email}</td>
                            )}
                            <td>{s.enrollmentDate}</td>
                            <td>{s.finalGrade != null ? s.finalGrade : '-'}</td>
                        </tr>
                    ))
                )}
                </tbody>
            </table>

            <Link className="btn btn-back" to="/courses">Back to Courses List</Link>
        </div>
    );
}

export default CoursesDetails;