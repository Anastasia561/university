import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                let res;
                if (auth?.accessToken) {
                    res = await authFetch('/api/courses', {}, auth, setAuth);
                } else {
                    res = await fetch('/api/courses');
                }

                const data = await res.json();

                setCourses(Array.isArray(data) ? data : []);
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
    }, [auth?.accessToken]);

    if (loading) {
        return <p>Loading courses...</p>;
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
                            <td>{index + 1}</td>
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
        </>
    );
}

export default Courses;