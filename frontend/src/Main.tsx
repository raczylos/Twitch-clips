import React from 'react'
import ReactDOM from 'react-dom/client'

import Root from "./components/Root"


import PopularClips from './pages/Clips/PopularClips'
import Login from './pages/Login/Login';
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


const rootElement = document.getElementById('root');
if (rootElement) {
	ReactDOM.createRoot(rootElement).render(
		<Main />
	);
} else {
	console.error("Root element not found");
}

