import React from 'react'
import '../../styles/DetailedViewStyles.css';
import {Link} from 'react-router-dom';

function StudentsDetails() {
    return (
        <React.Fragment>
            <div className="container">
                <h1 className="header">Student Details</h1>

                <ul>
                    <li><strong>First Name:</strong> Anna</li>
                    <li><strong>Last Name:</strong> Kowalska</li>
                    <li><strong>Email:</strong> anna.kowalska@example.com</li>
                    <li><strong>Student Number:</strong> s30200</li>
                    <li><strong>Birth Date:</strong> 2001-05-12</li>
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
                    <tr>
                        <td>ALG</td>
                        <td>2024-02-10</td>
                        <td>3</td>
                    </tr>
                    <tr>
                        <td>APBD</td>
                        <td>2024-03-15</td>
                        <td>4.5</td>
                    </tr>
                    <tr>
                        <td>SYC</td>
                        <td>2024-04-20</td>
                        <td>4</td>
                    </tr>
                    </tbody>
                </table>

                <Link className="btn  btn-back" to="/students">Back to Students List</Link>

            </div>
        </React.Fragment>
    )
}

export default StudentsDetails;