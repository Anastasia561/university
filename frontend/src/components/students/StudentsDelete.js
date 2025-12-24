import React from 'react';
import {useNavigate} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';

function StudentsDelete() {
    const navigate = useNavigate();

    const handleDelete = (e) => {
        e.preventDefault();
        //api call
        alert('Student deleted!');
        navigate('/students');
    };

    return (
        <div className="confirm-box">
            <h2>Confirm Deletion</h2>
            <p>Are you sure you want to delete this student?</p>

            <ul>
                <li><strong>First Name:</strong> Anna</li>
                <li><strong>Last Name:</strong> Kowalska</li>
                <li><strong>Email:</strong> anna.kowalska@example.com</li>
                <li><strong>Birth Date:</strong> 2001-05-12</li>
            </ul>

            <div>
                <button onClick={handleDelete} className="btn btn-delete">Delete</button>
                <button onClick={() => navigate('/students')} className="btn btn-cancel">Cancel</button>
            </div>
        </div>
    );
}

export default StudentsDelete;