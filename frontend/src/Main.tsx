import React from 'react'
import ReactDOM from 'react-dom/client'

import Root from "./components/Root"


// import ClipDetails from './pages/Clips/ClipDetails';
import PopularClips from './pages/Clips/PopularClips'
import Login from './pages/Login';
import ProtectedRoute from './ProtectedRoute';
import { BrowserRouter, Routes, Route } from 'react-router-dom';


import "./index.css"
import './styles.css'



function Main() {

    return(
        <BrowserRouter>
            <Routes>
                <Route element={<Root />} path="/">
                    <Route element={<Login />} path="/login"></Route>
                    <Route element={<ProtectedRoute />}>
                        <Route element={<PopularClips />} path="/popular"></Route>
                        {/* <Route element={<ClipDetails />} path="/clip/:id"></Route> */}
                    </Route>
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default Main;


ReactDOM.createRoot(document.getElementById('root')).render(
    <Main />
);

