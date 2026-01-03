import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function Students() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchEnrollments = async () => {
            try {
                let path = `/api/students?page=${currentPage}&size=${pageSize}`;
                const res = await authFetch(path, {}, auth, setAuth);
                const data = await res.json();

                setStudents(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setStudents([]);
            } finally {
                setLoading(false);
            }
        };

        fetchEnrollments();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>Loading students...</p>;
    }

    return (
        <>
            <h1>Students Data</h1>

            <div className="table-container">
                <div className="toolbar">
                    <Link className="link create-link" to="/students/create">Add Student</Link>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>First Name</th>
                        <th>Last Name</th>
                        <th>Email</th>
                        <th colSpan="3"></th>
                    </tr>
                    </thead>

                    <tbody>
                    {Array.isArray(students) && students.map((student, index) => (
                        <tr key={student.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
                            <td>{student.firstName}</td>
                            <td>{student.lastName}</td>
                            <td>{student.email}</td>
                            <td>
                                <Link className="link delete-link" to={`/students/${student.id}/delete`}>Delete</Link>
                            </td>
                            <td>
                                <Link className="link update-link" to={`/students/${student.id}/edit`}>Update</Link>
                            </td>
                            <td>
                                <Link className="link view-link" to={`/students/${student.id}`}>Details</Link>
                            </td>
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

export default Students;
