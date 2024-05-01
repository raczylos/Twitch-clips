import { useEffect } from 'react';
import useFetch from '../useFetch';
import Cookies from 'js-cookie';
// import { useNavigate } from 'react-router-dom';


const Logout = () => {

    // const navigateTo  = useNavigate();
    const {error, fetchData, status } = useFetch();
    
    const handleLogout = async () => {
        await fetchData(`http://localhost:8080/api/v1/auth/logout`, 'POST');

    };

    useEffect(() => {
        if (status === 200) {
            Cookies.remove('accessToken');
            Cookies.remove('refreshToken');
            Cookies.remove('twitchAccessToken');
            Cookies.remove('twitchRefreshToken');
            console.log("logged out successfully")
            // navigateTo('/');
            window.location.href = "http://127.0.0.1:5173/"
            
        }
        if (error) {
            console.log('Error:', error);
        }
    }, [status, error]);

    return (
        <button onClick={handleLogout}>
            Log Out
        </button>
    );
};

export default Logout;