import React from 'react'

import Header from '../components/Header'
import Home from '../components/Home'
import StudentsRoutes from './StudentRoutes';

import '../styles/GeneralStyles.css';

import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom'

function Main() {
    return (
        <React.Fragment>
            <Router>
                <Header/>
                <Routes>
                    <Route path="/home" element={<Home />} />
                    <Route path="/*" element={<StudentsRoutes />} />
                    {/*<Route path="/*" element={<CoursesRoutes />} />*/}
                    <Route path="/" element={<Navigate to="/home" replace />} />
                </Routes>
            </Router>
        </React.Fragment>
    )
}

export default Main;