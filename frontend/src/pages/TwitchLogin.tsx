import React from 'react';
import { useEffect, useState } from 'react';
import useFetch from '../hooks/useFetch';
import Cookies from 'js-cookie';

const TwitchLogin = () => {

    const { data, isLoading, error, fetchData } = useFetch();

    const tokens = ['twitchAccessToken', 'twitchRefreshToken', 'accessToken', 'refreshToken'];

    useEffect(() => {
        if(tokens.every(token => window.location.href.includes(token))){
            const urlParams = new URLSearchParams(window.location.search);
            const twitchAccessToken = urlParams.get('twitchAccessToken');
            const twitchRefreshToken = urlParams.get('twitchRefreshToken');
            const accessToken = urlParams.get('accessToken');
            const refreshToken = urlParams.get('refreshToken');

            fetchData(`http://localhost:8080/api/v1/auth/validate/twitch?accessToken=${twitchAccessToken}`, 'post');
            
            if(twitchAccessToken && twitchRefreshToken && accessToken && refreshToken) {
                Cookies.set('twitchAccessToken', twitchAccessToken , { expires: 1, secure: true, sameSite: 'strict' })
                Cookies.set('twitchRefreshToken', twitchRefreshToken, { expires: 10, secure: true, sameSite: 'strict' })
                Cookies.set('accessToken', accessToken, { expires: 1, secure: true, sameSite: 'strict' })
                Cookies.set('refreshToken', refreshToken, { expires: 10, secure: true, sameSite: 'strict' })
            }
        }

    }, []);

    useEffect(() => {

        if (data) {
            console.log('Response from server1:', data);
            window.location.href = "http://127.0.0.1:5173/"
        }
        if (error) {
            console.log('Error from server:', error);
        }

    }, [data, error]);

    const handleTwitchLogin =  () => {
        const redirectUri = "http://localhost:8080/api/v1/auth/login/oauth2/code/twitch"
        const clientId = process.env.TWITCH_CLIENT_ID
        const authUrl = `https://id.twitch.tv/oauth2/authorize?client_id=${clientId}&response_type=code&redirect_uri=${redirectUri}&scope=user:read:email+user:read:follows`;

        window.location.href = authUrl

    }

    return (
        <div>
            <button onClick={handleTwitchLogin}>Login with Twitch</button>
        </div>
    );
};

export default TwitchLogin;