import React, {useState, useEffect} from 'react';
import '../../styles/FormStyles.css';
import {useNavigate, useParams, Link} from 'react-router-dom';
import {validateEnrollment} from "../../validation/EnrollmentValidation";

function EnrollmentsUpdate() {
    const {id} = useParams(); // enrollment ID
    const navigate = useNavigate();

    const [serverMessage, setServerMessage] = useState('');
    const [errors, setErrors] = useState({});
    const [students, setStudents] = useState([]);
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);
    const [enrollment, setEnrollment] = useState({
        courseCode: '',
        studentEmail: '',
        enrollmentDate: '',
        finalGrade: ''
    });

    useEffect(() => {
        fetch('/api/students')
            .then(res => res.json())
            .then(data => setStudents(data))
            .catch(err => console.error(err));

        fetch('/api/courses')
            .then(res => res.json())
            .then(data => setCourses(data))
            .catch(err => console.error(err));
    }, []);

    useEffect(() => {
        const fetchCourse = async () => {
            try {
                const res = await fetch(`/api/enrollments/${id}`);
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
        fetchCourse();
    }, [id]);

    const handleChange = (e) => {
        setEnrollment({
            ...enrollment,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        const validationErrors = validateEnrollment(enrollment);
        setErrors(validationErrors);

        if (Object.keys(validationErrors).length > 0) {
            setServerMessage("Validation error");
            return;
        }

        fetch(`/api/enrollments/${id}`, {
            method: 'PUT',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify(enrollment)
        })
            .then(async res => {
                if (!res.ok) {
                    const data = await res.json();
                    if (data.fieldErrors) setErrors(data.fieldErrors);
                    if (data.message) setServerMessage(data.message);
                } else {
                    navigate('/enrollments');
                }
            })
            .catch(err => console.error(err));
    };

    if (loading) return <p>Loading enrollment data...</p>;

    return (
        <div className="container">
            <h1>Update Enrollment</h1>

            {serverMessage && <div className="error general-error">{serverMessage}</div>}

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="studentEmail">Student Email:</label>
                <select id="studentEmail" name="studentEmail" value={enrollment.studentEmail}
                        onChange={handleChange} required>
                    <option value="">Select Student</option>
                    {students.map(s => (
                        <option key={s.id} value={s.email}>{s.email}</option>
                    ))}
                </select>
                {errors.studentEmail && <span className="error">{errors.studentEmail}</span>}

                <label htmlFor="courseCode">Course Code:</label>
                <select id="courseCode" name="courseCode" value={enrollment.courseCode}
                        onChange={handleChange} required>
                    <option value="">Select Course</option>
                    {courses.map(c => (
                        <option key={c.id} value={c.code}>{c.code}</option>
                    ))}
                </select>
                {errors.courseCode && <span className="error">{errors.courseCode}</span>}

                <label htmlFor="enrollmentDate">Enrollment Date:</label>
                <input type="date" id="enrollmentDate" name="enrollmentDate"
                       value={enrollment.enrollmentDate} onChange={handleChange} required/>
                {errors.enrollmentDate && <span className="error">{errors.enrollmentDate}</span>}

                <label htmlFor="finalGrade">Final Grade:</label>
                <select id="finalGrade" name="finalGrade" value={enrollment.finalGrade || ''}
                        onChange={handleChange}>
                    <option value="">Select Grade</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="3.5">3.5</option>
                    <option value="4">4</option>
                    <option value="4.5">4.5</option>
                    <option value="5">5</option>
                </select>

                <button type="submit" className="btn btn-submit">Update</button>
                <Link className="btn btn-cancel" to="/enrollments">Cancel</Link>
            </form>
        </div>
    );
}

export default EnrollmentsUpdate;
