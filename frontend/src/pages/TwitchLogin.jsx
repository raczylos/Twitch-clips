import React from 'react';
import { useEffect, useState } from 'react';
import Cookies from 'js-cookie';

const TwitchLogin = () => {

    const [twitchAccessToken, setTwitchAccessToken] = useState('')
    const [twitchRefreshToken, setTwitchRefreshToken] = useState('')
    const [jwtToken, setJwtToken] = useState('')
    
    useEffect(() => {
        if(window.location.href.includes('twitchAccessToken','twitchRefreshToken', 'jwtToken')){
            const urlParams = new URLSearchParams(window.location.search);
            const twitchAccessToken = urlParams.get('twitchAccessToken');
            const twitchRefreshToken = urlParams.get('twitchRefreshToken');
            const jwtToken = urlParams.get('jwtToken');
            
            setTwitchAccessToken(twitchAccessToken)
            setTwitchRefreshToken(twitchRefreshToken)
            setJwtToken(jwtToken)
  

            Cookies.set('twitchAccessToken', twitchAccessToken , { expires: 1, secure: true, sameSite: 'strict' })
            Cookies.set('twitchRefreshToken', twitchRefreshToken, { expires: 10, secure: true, sameSite: 'strict' })
            Cookies.set('jwtToken', jwtToken, { expires: 1, secure: true, sameSite: 'strict' })
        }

    }, []);

    const handleTwitchLogin =  () => {
        const redirectUri = "http://localhost:8080/api/v1/auth/login/oauth2/code/twitch"
        const clientId = "fkp6o4vmbky1nst0m4nz23es6lxg01"
        const authUrl = `https://id.twitch.tv/oauth2/authorize?client_id=${clientId}&response_type=code&redirect_uri=${redirectUri}&scope=user:read:email user:read:follows`;


        window.location.href = authUrl

    }

    return (
        <div>
            <button onClick={handleTwitchLogin}>Login with Twitch</button>
        </div>
    );
};

export default TwitchLogin;