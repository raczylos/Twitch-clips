import { Outlet, useLocation } from "react-router-dom";
import Navbar from "./Navbar";
import Home from "../pages/Home/Home";
import React from 'react';

export default function Root() {

	const location = useLocation();
    const isHomePage = location.pathname === '/';

	return (
		<>
			<Navbar />
            <div className="main-container">
				{isHomePage && <Home />}
                <Outlet />
            </div>
			
		</>
	);
}
