import React from 'react'
import '../styles/HeaderStyles.css';
import { Link } from 'react-router-dom';

function Header() {
    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/home" className="nav-logo">University</Link>
                <ul className="nav-links">
                    <li><Link to="/students">Students</Link></li>
                    <li><Link to="/courses">Courses</Link></li>
                    <li><Link to="/enrollments">Enrollments</Link></li>
                </ul>
            </div>
        </nav>
    )
}

export default Header;