import '../../styles/DetailedViewStyles.css';
import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';

function StudentsDetails() {
    const { id } = useParams();

    const [student, setStudent] = useState(null);

    useEffect(() => {
        fetch(`/api/students/details/${id}`)
            .then(res => res.json())
            .then(data => setStudent(data))
            .catch(err => console.error(err));
    }, [id]);

    if (!student) {
        return <p>Loading student details...</p>;
    }

    return (
        <div className="container">
            <h1 className="header">Student Details</h1>

            <ul>
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
                    <th>Date</th>
                    <th>Final Grade</th>
                </tr>
                </thead>
                <tbody>
                {student.enrollments.length === 0 ? (
                    <tr>
                        <td colSpan="3">No enrollments</td>
                    </tr>
                ) : (
                    student.enrollments.map((e, index) => (
                        <tr key={index}>
                            <td>{e.courseCode}</td>
                            <td>{e.enrollmentDate}</td>
                            <td>{e.finalGrade}</td>
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