import React from 'react'
import ReactDOM from 'react-dom/client'
// import App from './App.jsx'
// import './index.css'
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Root from "./components/Root"

import './styles.css'
import ClipList from "./pages/Clips/ClipList"	
import ClipDetails from './pages/Clips/ClipDetails';
import FavoriteClips from './pages/Clips/FavoriteClips';

const router = createBrowserRouter([
	{	
		path: "/",
		element: <Root />,
		children: [
		{
			path: "/favorite-clips",
			element: <FavoriteClips/>,
		},
		{
			path: "/clip/:id",
			element: <ClipDetails />,
		}],
	},
]);


ReactDOM.createRoot(document.getElementById('root')).render(
	
	<React.StrictMode>
		{/* <Navbar/> */}
		<RouterProvider router={router} />
	</React.StrictMode>,
)
