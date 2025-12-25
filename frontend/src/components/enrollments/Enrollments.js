import React, {useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link} from 'react-router-dom';

function Enrollments() {
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/enrollments')
            .then(res => res.json())
            .then(data => {
                setEnrollments(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p>Loading enrollments...</p>;
    }

    return (
        <React.Fragment>
            <h1>Enrollments Data</h1>

            <div className="table-container">
                <div className="toolbar">
                    <Link className="link create-link" to="/enrollments/create">Add Enrollment</Link>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Course code</th>
                        <th>Student email</th>
                        <th>Due Date</th>
                        <th>Final grade</th>
                        <th colSpan="4"></th>
                    </tr>
                    </thead>

                    <tbody>
                    {enrollments.map((enrollment, index) => (
                        <tr key={enrollment.id}>
                            <td>{index + 1}</td>
                            <td>{enrollment.courseCode}</td>
                            <td>{enrollment.studentEmail}</td>
                            <td>{enrollment.enrollmentDate}</td>
                            <td>{enrollment.finalGrade != null ? enrollment.finalGrade : '-'}</td>
                            <td>
                                <Link className="link delete-link"
                                      to={`/enrollments/${enrollment.id}/delete`}>Delete</Link>
                            </td>
                            <td>
                                <Link className="link update-link"
                                      to={`/enrollments/${enrollment.id}/edit`}>Update</Link>
                            </td>
                            <td>
                                <Link className="link view-link" to={`/enrollments/${enrollment.id}`}>Details</Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </React.Fragment>
    );
}

export default Enrollments;