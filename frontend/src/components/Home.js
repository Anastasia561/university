import React from 'react'
import '../styles/HomeStyles.css';

function Home() {
    return (
        <React.Fragment>
            <div className="img">
                <div className="img-text">
                    <h1>Welcome to University Admin Panel</h1>
                    <p>Manage students, courses, and enrollments with ease</p>
                </div>
            </div>

            <div className="container">
                <p>
                    This administrative system allows you to efficiently manage university operations.
                    You can browse and update student data, courses, enrollment details.
                    Use the navigation panel at the top to get started.
                </p>
            </div>
        </React.Fragment>
    )
}

export default Home;