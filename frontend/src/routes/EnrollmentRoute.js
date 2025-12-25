import React from 'react'

import Enrollments from '../components/enrollments/Enrollments'
import EnrollmentsDetails from "../components/enrollments/EnrollmentsDetails";

import {Routes, Route} from 'react-router-dom'

function EnrollmentRoutes() {
    return (
        <Routes>
            <Route index element={<Enrollments/>}/>
            <Route path=":id" element={<EnrollmentsDetails/>}/>
            {/*<Route path="create" element={<CoursesCreate/>}/>*/}
            {/*<Route path=":id/edit" element={<CoursesUpdate/>}/>*/}
            {/*<Route path=":id/delete" element={<CoursesDelete/>}/>*/}
        </Routes>
    )
}

export default EnrollmentRoutes;