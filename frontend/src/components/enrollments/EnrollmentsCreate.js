import React, {useState, useEffect, useContext} from 'react';
import '../../styles/FormStyles.css';
import {useNavigate, Link} from 'react-router-dom';
import {validateEnrollment} from "../../validation/EnrollmentValidation";
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";

function EnrollmentsCreate() {
    const navigate = useNavigate();
    const {auth, setAuth} = useContext(AuthContext);

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [enrollment, setEnrollment] = useState({
        courseCode: '',
        studentEmail: '',
        enrollmentDate: '',
        finalGrade: ''
    });

    useEffect(() => {
        const fetchData = async () => {
            try {
                const studentsRes = await authFetch('/api/students/all', {}, auth, setAuth);
                const studentsData = await studentsRes.json();
                setStudents(Array.isArray(studentsData) ? studentsData : []);

                const coursesRes = await authFetch('/api/courses/all', {}, auth, setAuth);
                const coursesData = await coursesRes.json();
                setCourses(Array.isArray(coursesData) ? coursesData : []);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
            }
        };

        fetchData();
    }, [auth?.accessToken]);

    const handleChange = (e) => {
        setEnrollment({
            ...enrollment,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        const validationErrors = validateEnrollment(enrollment);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage("Validation error");
            return;
        }

        try {
            const res = await authFetch('/api/enrollments', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(enrollment)
            }, auth, setAuth);

            if (!res.ok) {
                const data = await res.json();
                if (data.fieldErrors) setErrors(data.fieldErrors);
                if (data.message) setServerMessage(data.message);
            } else {
                navigate('/enrollments');
            }
        } catch (err) {
            if (err.message === 'Session expired') {
                navigate('/login');
            }
            console.error(err);
        }
    };

    return (
        <div className="container">
            <h1>Create Enrollment</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="studentEmail">Student Email:</label>
                <select id="studentEmail" name="studentEmail"
                        value={enrollment.studentEmail} onChange={handleChange} required>
                    <option value="">Select Student</option>
                    {Array.isArray(students) && students.map((s) => (
                        <option key={s.id} value={s.email}>{s.email}</option>
                    ))}
                </select>
                {errors.studentEmail && <span className="error">{errors.studentEmail}</span>}

                <label htmlFor="courseCode">Course Code:</label>
                <select id="courseCode" name="courseCode"
                        value={enrollment.courseCode} onChange={handleChange} size={5} required>
                    <option value="">Select Course</option>
                    {Array.isArray(courses) && courses.map((c) => (
                        <option key={c.id} value={c.code}>{c.code}</option>
                    ))}
                </select>
                {errors.courseCode && <span className="error">{errors.courseCode}</span>}

                <label htmlFor="enrollmentDate">Enrollment Date:</label>
                <input type="date" id="enrollmentDate" name="enrollmentDate"
                       value={enrollment.enrollmentDate} onChange={handleChange} required/>
                {errors.enrollmentDate && <span className="error">{errors.enrollmentDate}</span>}

                <label htmlFor="finalGrade">Final Grade:</label>
                <select id="finalGrade" name="finalGrade" value={enrollment.finalGrade} onChange={handleChange}>
                    <option value="">Select Grade</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="3.5">3.5</option>
                    <option value="4">4</option>
                    <option value="4.5">4.5</option>
                    <option value="5">5</option>
                </select>

                <button type="submit" className="btn btn-submit">Create</button>
                <Link className="btn btn-cancel" to="/enrollments">Cancel</Link>
            </form>
        </div>
    );
}

export default EnrollmentsCreate;