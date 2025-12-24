import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';

function StudentsDelete() {
    const {id} = useParams();
    const navigate = useNavigate();

    const [student, setStudent] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch(`/api/students/${id}`, {
                method: 'DELETE',
            });
            if (!res.ok) {
                const data = await res.json();
                setServerMessage(data.message);
            } else {
                navigate('/students');
            }
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        const fetchStudent = async () => {
            try {
                const res = await fetch(`/api/students/details/${id}`);
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                }
                setStudent(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchStudent();
    }, [id]);

    if (loading) return <p>Loading student data...</p>;

    return (
        <div className="confirm-box">
            <h2>Confirm Deletion</h2>
            <p>Are you sure you want to delete this student?</p>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <ul>
                <li><strong>First Name: </strong>{student.firstName}</li>
                <li><strong>Last Name: </strong>{student.lastName}</li>
                <li><strong>Email: </strong>{student.email}</li>
                <li><strong>Birth Date: </strong>{student.birthdate}</li>
            </ul>

            <div>
                <button onClick={handleDelete} className="btn btn-delete">Delete</button>
                <button onClick={() => navigate('/students')} className="btn btn-cancel">Cancel</button>
            </div>
        </div>
    );
}

export default StudentsDelete;