import React, {useState} from 'react';
import '../../styles/FormStyles.css';
import {Link, useNavigate} from 'react-router-dom';

function StudentsUpdate({student}) {
    const navigate = useNavigate();

    // const [firstName, setFirstName] = useState(student.firstName);
    // const [lastName, setLastName] = useState(student.lastName);
    // const [email, setEmail] = useState(student.email);
    // const [birthDate, setBirthDate] = useState(student.birthDate);

    const handleSubmit = (e) => {
        e.preventDefault();
        // api call
        alert(`Updated student`);
        navigate('/students');
    };

    return (
        <div className="container">
            <h1>Update Student</h1>

            <form onSubmit={handleSubmit} noValidate>
                <label htmlFor="first_name">First Name:</label>
                <input
                    id="first_name"
                    name="first_name"
                    type="text"
                    // value={firstName}
                    // onChange={(e) => setFirstName(e.target.value)}
                    required
                />

                <label htmlFor="last_name">Last Name:</label>
                <input
                    id="last_name"
                    name="last_name"
                    type="text"
                    // value={lastName}
                    // onChange={(e) => setLastName(e.target.value)}
                    required
                />

                <label htmlFor="email">Email:</label>
                <input
                    id="email"
                    name="email"
                    type="email"
                    // value={email}
                    // onChange={(e) => setEmail(e.target.value)}
                    required
                />

                <label htmlFor="birth_date">Birth Date:</label>
                <input
                    id="birth_date"
                    name="birth_date"
                    type="date"
                    // value={birthDate}
                    // onChange={(e) => setBirthDate(e.target.value)}
                    required
                />

                <button className="btn btn-submit" type="submit">Update</button>
                <Link className="btn btn-cancel" to="/students">Cancel</Link>
            </form>
        </div>
    );
}

export default StudentsUpdate;