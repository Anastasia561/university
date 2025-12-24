import React from 'react'
import '../../styles/TableStyles.css';
import { Link } from 'react-router-dom';

function Students() {
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
                        <th>Student Number</th>
                        <th colSpan="3" style={{ backgroundColor: 'white' }}></th>
                    </tr>
                    </thead>

                    <tbody>
                    <tr>
                        <td>1</td>
                        <td>Anna</td>
                        <td>Kowalska</td>
                        <td>anna.kowalska@example.com</td>
                        <td>s30400</td>
                        <td><Link className="link delete-link" to={`/students/${1}/delete`}>Delete</Link></td>
                        <td><Link className="link update-link" to={`/students/${1}/update`}>Update</Link></td>
                        <td><Link className="link view-link" to={`/students/${1}/details`}>Details</Link></td>
                    </tr>
                    </tbody>
                </table>

            </div>
        </React.Fragment>
    )
}

export default Students;