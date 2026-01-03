import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                let res;
                let path = `/api/courses?page=${currentPage}&size=${pageSize}`;
                if (auth?.accessToken) {
                    res = await authFetch(path, {}, auth, setAuth);
                } else {
                    res = await fetch(path);
                }

                const data = await res.json();

                setCourses(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setCourses([]);
            } finally {
                setLoading(false);
            }
        };

        fetchCourses();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>Loading courses...</p>;
    }

    if (!Array.isArray(courses) || courses.length === 0) {
        return <h1>No courses available</h1>;
    }

    return (
        <>
            <h1>Courses Data</h1>

            <div className="table-container">
                {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                    <div className="toolbar">
                        <Link className="link create-link" to="/courses/create">Add Course</Link>
                    </div>
                )}

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Credit</th>
                        {auth.accessToken && (
                            <th colSpan="3"></th>
                        )}
                    </tr>
                    </thead>

                    <tbody>

                    {Array.isArray(courses) && courses.map((course, index) => (
                        <tr key={course.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
                            <td>{course.name}</td>
                            <td>{course.code}</td>
                            <td>{course.credit}</td>
                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <>
                                    <td>
                                        <Link className="link delete-link"
                                              to={`/courses/${course.id}/delete`}>Delete</Link>
                                    </td>
                                    <td>
                                        <Link className="link update-link"
                                              to={`/courses/${course.id}/edit`}>Update</Link>
                                    </td>
                                </>
                            )}

                            {auth.accessToken && (auth.role === "ROLE_STUDENT" || auth.role === "ROLE_ADMIN") && (
                                <td>
                                    <Link className="link view-link" to={`/courses/${course.id}`}>Details</Link>
                                </td>
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

export default Courses;