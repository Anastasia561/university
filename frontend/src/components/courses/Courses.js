import React, {useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link} from 'react-router-dom';

function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/courses')
            .then(res => res.json())
            .then(data => {
                setCourses(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p>Loading courses...</p>;
    }

    return (
        <React.Fragment>
            <h1>Courses Data</h1>

            <div className="table-container">
                <div className="toolbar">
                    <Link className="link create-link" to="/courses/create">Add Course</Link>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Code</th>
                        <th>Credit</th>
                        <th colSpan="3"></th>
                    </tr>
                    </thead>

                    <tbody>
                    {courses.map((course, index) => (
                        <tr key={course.id}>
                            <td>{index + 1}</td>
                            <td>{course.name}</td>
                            <td>{course.code}</td>
                            <td>{course.credit}</td>
                            <td>
                                <Link className="link delete-link" to={`/courses/${course.id}/delete`}>Delete</Link>
                            </td>
                            <td>
                                <Link className="link update-link" to={`/courses/${course.id}/update`}>Update</Link>
                            </td>
                            <td>
                                <Link className="link view-link" to={`/courses/${course.id}`}>Details</Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </React.Fragment>
    );
}

export default Courses;