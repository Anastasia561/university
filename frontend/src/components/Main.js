import React from 'react'

import Header from './Header'
import Home from './Home'
import Students from './students/Students'
import StudentsDetails from './students/StudentsDetails'
import StudentsDelete from './students/StudentsDelete'
import StudentsUpdate from './students/StudentsUpdate'
import StudentsCreate from './students/StudentsCreate'

import '../styles/GeneralStyles.css';

import {BrowserRouter as Router, Routes, Route, Navigate} from 'react-router-dom'

function Main() {
    return (
        <React.Fragment>
            <Router>
                <Header/>
                <Routes>
                    <Route path="/home" element={<Home/>}/>
                    <Route path="/students" element={<Students/>}/>
                    <Route path="/students/:id/details" element={<StudentsDetails/>}/>
                    <Route path="/students/:id/delete" element={<StudentsDelete/>}/>
                    <Route path="/students/:id/update" element={<StudentsUpdate/>}/>
                    <Route path="/students/create" element={<StudentsCreate/>}/>
                    <Route path="*" element={<Navigate to="/home" replace/>}/>
                </Routes>
            </Router>
        </React.Fragment>
    )
}

export default Main;