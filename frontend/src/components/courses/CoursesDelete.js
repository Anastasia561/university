import React, {useContext, useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import '../../styles/ConfirmStyles.css';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function CoursesDelete() {
    const {id} = useParams();
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);

    const [course, setCourse] = useState(null);
    const [serverMessage, setServerMessage] = useState('');
    const [loading, setLoading] = useState(true);

    const handleDelete = async (e) => {
        e.preventDefault();
        try {
            const res = await authFetch(`/api/courses/${id}`, {
                    method: 'DELETE'
                },
                auth, setAuth
            );
            if (!res.ok) {
                const data = await res.json();
                setServerMessage(data.message);
            } else {
                navigate('/courses');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.log(err);
        }
    };

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await authFetch(`/api/courses/${id}`, {
                        headers: {
                            'Content-Type': 'application/json'
                        },
                    },
                    auth, setAuth
                );
                const data = await res.json();
                if (!res.ok) {
                    setServerMessage(data.message);
                }
                setCourse(data);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchCourse();
    }, [id]);

    if (loading) return <p>Loading course data...</p>;

    return (
        <div className="confirm-box">
            <h2>Confirm Deletion</h2>

            {serverMessage ? (
                <div className="error general-error">{serverMessage}</div>
            ) : (
                <>
                    <p>Are you sure you want to delete this course?</p>
                    <ul>
                        <li><strong>Course Name: </strong>{course.name}</li>
                        <li><strong>Course Code: </strong>{course.code}</li>
                        <li><strong>Credit: </strong>{course.credit}</li>
                    </ul>

                    <div>
                        <button onClick={handleDelete} className="btn btn-delete">Delete</button>
                        <button onClick={() => navigate('/courses')} className="btn btn-cancel">Cancel</button>
                    </div>
                </>
            )}
        </div>
    );
}

export default CoursesDelete;