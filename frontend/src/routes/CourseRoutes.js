import React from 'react'

import Courses from '../components/courses/Courses'
import CoursesDetails from "../components/courses/CoursesDetails";
// import StudentsDetails from '../components/students/StudentsDetails'
// import StudentsDelete from '../components/students/StudentsDelete'
// import StudentsUpdate from '../components/students/StudentsUpdate'
// import StudentsCreate from '../components/students/StudentsCreate'

import {Routes, Route} from 'react-router-dom'

function CourseRoutes() {
    return (
        <Routes>
            <Route index element={<Courses />} />
            {/*<Route path="/students/create" element={<StudentsCreate/>}/>*/}
            <Route path=":id" element={<CoursesDetails/>}/>
            {/*<Route path="/students/:id/update" element={<StudentsUpdate/>}/>*/}
            {/*<Route path="/students/:id/delete" element={<StudentsDelete/>}/>*/}
        </Routes>
    )
}

export default CourseRoutes;