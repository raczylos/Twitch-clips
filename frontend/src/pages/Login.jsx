import  { useState, useEffect } from 'react';
import useFetch from '../useFetch';
import TwitchLogin from './TwitchLogin';
import Cookies from 'js-cookie';



const Login = () => {
    const twitchAccessToken = Cookies.get('twitchAccessToken');
    const twitchRefreshToken = Cookies.get('twitchRefreshToken');
    const jwtToken = Cookies.get('jwtToken');

    const [loginFormData, setLoginFormData] = useState({
        username: '',
        password: '',
    });

    // const {data, isLoading, error} = useFetch(`http://localhost:8080/api/v1/auth/authenticate?username=${loginFormData.username}&password=${loginFormData.password}`);
    const { data, isLoading, error, fetchData } = useFetch();

    const handleLoginSubmit = async (event) => {
        event.preventDefault();
    
        console.log('Username:', loginFormData.username);
        console.log('Password:', loginFormData.password);

        fetchData(`http://localhost:8080/api/v1/auth/authenticate?username=${loginFormData.username}&password=${loginFormData.password}`, "POST");



    };

    useEffect(() => {

        if (data) {
            console.log('Response from server:', data);
            // redirect to home page
        }
        if (error) {
            console.log('Error from server:', error);
        }

    }, [data, error]);
    

    return (
        <>
            <form onSubmit={handleLoginSubmit}>
            <label htmlFor="username">Username:</label>
            <input
                type="text"
                id="username"
                name="username"
                value={loginFormData.username}
                onChange={(event) => setLoginFormData({...loginFormData, username: event.target.value})}
            />


            <label htmlFor="password">Password:</label>
            <input
                type="password"
                id="password"
                name="password"
                value={loginFormData.password}
                onChange={(event) => setLoginFormData({...loginFormData, password: event.target.value})}
            />


            <button type="submit">Login</button>
        </form>

        <TwitchLogin />
        </>
        
    );
}

export default Login;

