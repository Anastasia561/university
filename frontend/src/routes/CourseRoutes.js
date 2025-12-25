import React from 'react'

import Courses from '../components/courses/Courses'
import CoursesDetails from "../components/courses/CoursesDetails";
import CoursesCreate from "../components/courses/CoursesCreate";
import CoursesUpdate from "../components/courses/CoursesUpdate";

import {Routes, Route} from 'react-router-dom'

function CourseRoutes() {
    return (
        <Routes>
            <Route index element={<Courses/>}/>
            <Route path=":id" element={<CoursesDetails/>}/>
            <Route path="create" element={<CoursesCreate/>}/>
            <Route path=":id/edit" element={<CoursesUpdate/>}/>
            {/*<Route path="/students/:id/delete" element={<StudentsDelete/>}/>*/}
        </Routes>
    )
}

export default CourseRoutes;