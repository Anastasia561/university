import React from 'react'
import Header from './Header'
import Home from './Home'
import Students from './Students'

import {BrowserRouter as Router, Routes, Route} from 'react-router-dom'

function Main() {
    return (
        <React.Fragment>
            <Router>
                <Header/>
                <Routes>
                    <Route path="/home" element={<Home/>}/>
                    <Route path="/students" element={<Students/>}/>
                </Routes>
            </Router>
        </React.Fragment>
    )
}

export default Main;