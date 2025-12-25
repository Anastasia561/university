import React from 'react'

import Header from '../components/Header'
import Home from '../components/Home'
import StudentRoutes from './StudentRoutes';
import CourseRoutes from "./CourseRoutes";
import EnrollmentRoutes from "./EnrollmentRoute";

import '../styles/GeneralStyles.css';

import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom'

function Main() {
    return (
        <Router>
            <Header/>
            <Routes>
                <Route path="/home" element={<Home/>}/>
                <Route path="/students/*" element={<StudentRoutes/>}/>
                <Route path="/courses/*" element={<CourseRoutes/>}/>
                <Route path="/enrollments/*" element={<EnrollmentRoutes/>}/>
                <Route path="/" element={<Navigate to="/home" replace/>}/>
            </Routes>
        </Router>
    )
}

export default Main;