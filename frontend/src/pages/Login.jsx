import  { useState, useEffect } from 'react';
import useFetch from '../Fetch';
import TwitchLogin from './TwitchLogin';
import Cookies from 'js-cookie';


const Login = () => {

    const [loginFormData, setLoginFormData] = useState({
        username: '',
        password: '',

    });


    useEffect(() => {
        const twitchAccessToken = Cookies.get('twitchAccessToken')
        const twitchRefreshToken = Cookies.get('twitchRefreshToken')
        const jwtToken = Cookies.get('jwtToken')
        if(twitchAccessToken && twitchRefreshToken && jwtToken) {
            console.log('twitchAccessToken', twitchAccessToken)
            console.log('twitchRefreshToken', twitchRefreshToken)
            console.log('jwtToken', jwtToken)
            
        }
        
    }, []);

    const handleLoginSubmit = async (event) => {
        event.preventDefault();
    
        console.log('Username:', loginFormData.username);
        console.log('Password:', loginFormData.password);

    };
    

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

