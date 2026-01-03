import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function Enrollments() {
    const navigate = useNavigate();
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);

    useEffect(() => {
        const fetchEnrollments = async () => {
            try {
                let path = `/api/enrollments?page=${currentPage}&size=${pageSize}`;

                const res = await authFetch(path, {}, auth, setAuth);
                const data = await res.json();

                setEnrollments(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setEnrollments([]);
            } finally {
                setLoading(false);
            }
        };

        fetchEnrollments();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>Loading enrollments...</p>;
    }

    if (!Array.isArray(enrollments) || enrollments.length === 0) {
        return <h1>No enrollments available</h1>;
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
                    {Array.isArray(enrollments) && enrollments.map((enrollment, index) => (
                        <tr key={enrollment.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
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

            <div className="pagination">
                <button
                    disabled={currentPage === 0 || loading}
                    onClick={() => setCurrentPage(prev => prev - 1)}>Previous
                </button>

                <span>Page {currentPage + 1} of {totalPages}</span>

                <button
                    disabled={currentPage + 1 >= totalPages || loading}
                    onClick={() => setCurrentPage(prev => prev + 1)}>Next
                </button>
            </div>
        </>
    );
}

export default Enrollments;