import React from 'react'

import Header from '../components/Header'
import Students from '../components/students/Students'
import StudentsDetails from '../components/students/StudentsDetails'
import StudentsDelete from '../components/students/StudentsDelete'
import StudentsUpdate from '../components/students/StudentsUpdate'
import StudentsCreate from '../components/students/StudentsCreate'

import '../styles/GeneralStyles.css';

import {Routes, Route} from 'react-router-dom'

function StudentRoutes() {
    return (
        <Routes>
            <Route path="/students" element={<Students/>}/>
            <Route path="/students/create" element={<StudentsCreate/>}/>
            <Route path="/students/:id/details" element={<StudentsDetails/>}/>
            <Route path="/students/:id/update" element={<StudentsUpdate/>}/>
            <Route path="/students/:id/delete" element={<StudentsDelete/>}/>
        </Routes>
    )
}

export default StudentRoutes;