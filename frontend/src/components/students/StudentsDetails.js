import '../../styles/DetailedViewStyles.css';
import React, {useEffect, useState} from 'react';
import {Link, useParams} from 'react-router-dom';

function StudentsDetails() {
    const {id} = useParams();

    const [serverMessage, setServerMessage] = useState('');
    const [student, setStudent] = useState(null);

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await fetch(`/api/students/details/${id}`);

                if (!res.ok) {
                    if (res.status === 404) {
                        setServerMessage('Student not found');
                    } else {
                        const data = await res.json();
                        setServerMessage(data.message);
                    }
                    setStudent(null);
                    return;
                }

                const data = await res.json();
                setStudent(data);
                setServerMessage('');
            } catch (err) {
                console.error(err);
                setServerMessage('Failed to fetch student details');
                setStudent(null);
            }
        };
        fetchStudent();
    }, [id]);

    if (!student) {
        return <p>Loading student details...</p>;
    }

    return (
        <div className="container">
            <h1 className="header">Student Details</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

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