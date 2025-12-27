import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";

function Enrollments() {
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);
    const {auth} = useContext(AuthContext);

    useEffect(() => {
        fetch('/api/enrollments', {
            headers: {
                Authorization: `Bearer ${auth.accessToken}`
            },
            credentials: 'include'
        })
            .then(res => res.json())
            .then(data => {
                setEnrollments(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, [auth.accessToken]);

    if (loading) {
        return <p>Loading enrollments...</p>;
    }

    return (
        <>
            <h1>Enrollments Data</h1>

            <div className="table-container">
                {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                    <div className="toolbar">
                        <Link className="link create-link" to="/enrollments/create">Add Enrollment</Link>
                    </div>
                )}

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Course code</th>
                        <th>Student email</th>
                        <th>Due Date</th>
                        <th>Final grade</th>
                        {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                            <th colSpan="4"></th>
                        )}

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

                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <>
                                    <td>
                                        <Link className="link delete-link"
                                              to={`/enrollments/${enrollment.id}/delete`}>Delete</Link>
                                    </td>
                                    <td>
                                        <Link className="link update-link"
                                              to={`/enrollments/${enrollment.id}/edit`}>Update</Link>
                                    </td>
                                    <td>
                                        <Link className="link view-link" to={`/enrollments/${enrollment.id}`}>Details
                                        </Link>
                                    </td>
                                </>

                            )}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </>
    );
}

export default Enrollments;