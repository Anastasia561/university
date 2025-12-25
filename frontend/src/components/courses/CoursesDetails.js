import '../../styles/DetailedViewStyles.css';
import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';

function CoursesDetails() {
    const {id} = useParams();

    const [serverMessage, setServerMessage] = useState('');
    const [course, setCourse] = useState(null);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await fetch(`/api/courses/details/${id}`);

                if (!res.ok) {
                    if (res.status === 404) {
                        setServerMessage('Course not found');
                    } else {
                        const data = await res.json();
                        setServerMessage(data.message);
                    }
                    return;
                }

                const data = await res.json();
                setCourse(data);
            } catch (err) {
                console.error(err);
            }
        };
        fetchCourse();
    }, [id]);

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

            <h2 className="header">Enrolled Students</h2>

            <table>
                <thead>
                <tr>
                    <th>Student Email</th>
                    <th>Final Grade</th>
                </tr>
                </thead>
                <tbody>
                {course.students.length === 0 ? (
                    <tr>
                        <td colSpan="2">No students</td>
                    </tr>
                ) : (
                    course.students.map((s, index) => (
                        <tr key={index}>
                            <td>{s.email}</td>
                            <td>{s.finalGrade}</td>
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