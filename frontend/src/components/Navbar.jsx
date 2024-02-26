import { Link } from "react-router-dom";

export default function Navbar() {
    return ( 
        <nav className="navbar">
            <Link to="/" className="site-title">Site title</Link>
            <ul>
                <li>
                    <Link to="/login">Login</Link>
                </li>
                <li>    
                    <Link to="/clips">Clips</Link>
                </li>
            </ul>
            
        </nav>
    )
}