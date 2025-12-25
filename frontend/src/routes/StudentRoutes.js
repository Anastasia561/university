import React from 'react'

import Students from '../components/students/Students'
import StudentsDetails from '../components/students/StudentsDetails'
import StudentsDelete from '../components/students/StudentsDelete'
import StudentsUpdate from '../components/students/StudentsUpdate'
import StudentsCreate from '../components/students/StudentsCreate'

import {Routes, Route} from 'react-router-dom'

function StudentRoutes() {
    return (
        <Routes>
            <Route index element={<Students />} />
            <Route path="create" element={<StudentsCreate/>}/>
            <Route path=":id" element={<StudentsDetails/>}/>
            <Route path=":id/edit" element={<StudentsUpdate/>}/>
            <Route path=":id/delete" element={<StudentsDelete/>}/>
        </Routes>
    )
}

export default StudentRoutes;