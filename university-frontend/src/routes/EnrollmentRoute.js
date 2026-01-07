import React from 'react'

import Enrollments from '../components/enrollments/Enrollments'
import EnrollmentsDetails from "../components/enrollments/EnrollmentsDetails";
import EnrollmentsCreate from "../components/enrollments/EnrollmentsCreate";
import EnrollmentsUpdate from "../components/enrollments/EnrollmentsUpdate";
import EnrollmentsDelete from "../components/enrollments/EnrollmentsDelete";

import {Routes, Route} from 'react-router-dom'

function EnrollmentRoutes() {
    return (
        <Routes>
            <Route index element={<Enrollments/>}/>
            <Route path=":id" element={<EnrollmentsDetails/>}/>
            <Route path="create" element={<EnrollmentsCreate/>}/>
            <Route path=":id/edit" element={<EnrollmentsUpdate/>}/>
            <Route path=":id/delete" element={<EnrollmentsDelete/>}/>
        </Routes>
    )
}

export default EnrollmentRoutes;