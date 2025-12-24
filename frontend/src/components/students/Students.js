import React, {useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link} from 'react-router-dom';

function Students() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('/api/students')
            .then(res => res.json())
            .then(data => {
                setStudents(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, []);

    if (loading) {
        return <p>Loading students...</p>;
    }

    return (
        <React.Fragment>
            <h1>Students Data</h1>

            <div className="table-container">
                <div className="toolbar">
                    <Link className="link create-link" to="/students/add">Add Student</Link>
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
                    {students.map((student, index) => (
                        <tr key={student.id}>
                            <td>{index + 1}</td>
                            <td>{student.firstName}</td>
                            <td>{student.lastName}</td>
                            <td>{student.email}</td>
                            <td>
                                <Link className="link delete-link" to={`/students/${student.id}/delete`}>Delete</Link>
                            </td>
                            <td>
                                <Link className="link update-link" to={`/students/${student.id}/update`}>Update</Link>
                            </td>
                            <td>
                                <Link className="link view-link" to={`/students/${student.id}/details`}>Details</Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </React.Fragment>
    );
}

export default Students;
