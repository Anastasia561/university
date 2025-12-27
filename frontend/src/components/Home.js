import React from 'react'
import '../styles/HomeStyles.css';

import {useContext} from "react";
import AuthContext from "../context/AuthProvider";

function Home() {
    const {auth} = useContext(AuthContext);

    return (
        <>
            <div className="img">
                <div className="img-text">
                    {auth?.role === "ROLE_ADMIN" && (
                        <>
                            <h1>Welcome to University Admin Panel</h1>
                            <p>Manage students, courses, and enrollments with ease</p>
                        </>
                    )}
                    {auth?.role === "ROLE_STUDENT" && (
                        <>
                            <h1>Welcome to University Student Panel</h1>
                            <p>Manage your courses and enrollment with ease</p>
                        </>
                    )}
                    {!auth?.role && (
                        <>
                            <h1>Welcome to University Panel</h1>
                            <p>Platform for course, student, enrollment management</p>
                        </>
                    )}
                </div>
            </div>

            <div className="container">
                {auth?.role === "ROLE_ADMIN" && (
                    <>
                        <p>This administrative system allows you to efficiently manage university operations></p>
                        <p>You can browse and update student data, courses, enrollment details<br/>
                            Use the navigation panel at the top to get started</p>
                    </>
                )}
                {auth?.role === "ROLE_STUDENT" && (
                    <>
                        <p>This administrative system allows you to efficiently manage university operations</p>
                        <p>You can browse courses and your enrollments<br/>
                            Use the navigation panel at the top to get started</p>
                    </>
                )}
                {!auth?.role && (
                    <>
                        <p>This administrative system allows you to efficiently manage university operations</p>
                        <p>Please log in to get started</p>
                    </>
                )}
            </div>
        </>
    );
}

export default Home;