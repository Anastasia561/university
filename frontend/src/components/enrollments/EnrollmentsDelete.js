import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';
import AuthContext from "../../context/AuthProvider";

function EnrollmentsDelete() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth} = useContext(AuthContext);

    const [enrollment, setEnrollment] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch(`/api/enrollments/${id}`, {
                method: 'DELETE',
                headers: {
                    Authorization: `Bearer ${auth.accessToken}`
                }
            });
            if (!res.ok) {
                const data = await res.json();
                setServerMessage(data.message);
            } else {
                navigate('/enrollments');
            }
        } catch (err) {
            console.log(err);
        }
    };

    useEffect(() => {
        const fetchEnrollment = async () => {
            try {
                const res = await fetch(`/api/enrollments/${id}`, {
                    headers: {
                        Authorization: `Bearer ${auth.accessToken}`
                    }
                });
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                }
                setEnrollment(data);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchEnrollment();
    }, [id, auth.accessToken]);

    if (loading) return <p>Loading enrollment data...</p>;

    return (
        <div className="confirm-box">
            <h2>Confirm Deletion</h2>

            {serverMessage ? (
                <div className="error general-error">{serverMessage}</div>
            ) : (
                <>
                    <p>Are you sure you want to delete this enrollment?</p>
                    <ul>
                        <li><strong>Course Code: </strong>{enrollment.courseCode}</li>
                        <li><strong>Student Email: </strong>{enrollment.studentEmail}</li>
                        <li><strong>Due Date: </strong>{enrollment.enrollmentDate}</li>
                        <li><strong>Final Grade: </strong>{enrollment.finalGrade}</li>
                    </ul>

                    <div>
                        <button onClick={handleDelete} className="btn btn-delete">Delete</button>
                        <button onClick={() => navigate('/enrollments')} className="btn btn-cancel">Cancel</button>
                    </div>
                </>
            )}
        </div>
    );
}

export default EnrollmentsDelete;