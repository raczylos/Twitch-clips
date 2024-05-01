import { Link } from "react-router-dom";
import Cookies from 'js-cookie';
import Logout from './Logout';
import  { useState, useEffect } from 'react';


export default function Navbar() {
    const [isLoggedIn, setIsLoggedIn]=useState(false)

    useEffect(() => {
        if(Cookies.get("accessToken")){
            setIsLoggedIn(true)
        } else {
            setIsLoggedIn(false)
        }
    }, [])

    return ( 
        <nav className="navbar">
            <Link to="/" className="site-title">Site title</Link>
            <ul>
                {!isLoggedIn && (
                    <li>
                        <Link to="/login">Login</Link>
                    </li>
                )}

                {isLoggedIn && (
                    <li>    
                        <Link to="/popular">Popular</Link>
                    </li>
                )}
                {isLoggedIn && (
                    <li>    
                        <Logout />
                    </li>
                )}
            </ul>
            
        </nav>
    )
}