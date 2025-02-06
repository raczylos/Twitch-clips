import { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';
import React from 'react';

type configType = {
    method: string,
    headers: {
        Authorization: string
    }
}

const useFetch = () => {
    const [data, setData] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [status, setStatus] = useState<number | null>(null);
    const fetchData = async (url: string, method: string) => {
        setIsLoading(true);
        setError(null);

        const accessToken = Cookies.get('accessToken');
        let config: configType = {
            method: method,
            headers: {
                Authorization: ''
            }
        };

        if (accessToken) {
            console.log("accessToken:", accessToken);
            config.headers.Authorization = `Bearer ${accessToken}`;
        }
        console.log("config", config)
        console.log("url", url)

        try {
            const response = await axios(url, config);
            setData(response.data);
            setStatus(response.status);
        } catch (error:  any) {
            if(error.response.status === 401 && error.response.data === "JWT expired") {
                try{
                    console.log("attempting to refresh jwt token")
                    const refreshToken = Cookies.get('refreshToken');
                    const refreshConfig  = {
                        headers: {
                            Authorization: `Bearer ${refreshToken}`
                        }
                    };
                    const response = await axios.post('http://localhost:8080/api/v1/auth/refresh-token', null, refreshConfig);

                    console.log("response", response)
                    const newAccessToken = response.data.accessToken;
                    if(newAccessToken){
                        console.log("setting new access token", newAccessToken)
                        Cookies.set('accessToken', newAccessToken);
                        config.headers.Authorization = `Bearer ${newAccessToken}`; 
                        const retryResponse = await axios(url, config); 
                        setData(retryResponse.data);
                        setStatus(retryResponse.status);
                    } else {
                        console.log("newAccessToken not found: ", newAccessToken)
                        Cookies.remove('accessToken');
                        Cookies.remove('refreshToken');
                        // window.location.href = "http://127.0.0.1:5173/"
                    }

                } catch (refreshTokenError: any) {
                    console.log("refreshTokenError", refreshTokenError)
                    if(refreshTokenError.response.status === 401) {
                        Cookies.remove('accessToken');
                        Cookies.remove('refreshToken');
                        Cookies.remove('twitchAccessToken');
                        Cookies.remove('twitchRefreshToken');
                        // window.location.href = "http://127.0.0.1:5173/"
                    }
                    setError(refreshTokenError);
                }
                
            } else if(error.response.status === 401 && error.response.data === "Invalid access token"){
                try {
                    const twitchRefreshToken = Cookies.get('twitchRefreshToken');
                    const refreshToken = Cookies.get('refreshToken');
                    const config = {
                        headers: {
                            Authorization: `Bearer ${refreshToken}`
                        },
                        params: {
                            twitchRefreshToken: twitchRefreshToken
                        }
                    };
                    const response = await axios.post('http://localhost:8080/api/v1/auth/twitch/refresh-token', null, config);
                    console.log("response", response)
                    const newTwitchAccessToken = response.data.accessToken;
                    if(newTwitchAccessToken){
                        Cookies.set('twitchAccessToken', newTwitchAccessToken);
                        config.headers.Authorization = `Bearer ${newTwitchAccessToken}`;
                        const retryResponse = await axios(url, config);
                        setData(retryResponse.data);
                        setStatus(retryResponse.status);
                    } else {
                        Cookies.remove('twitchAccessToken');
                        Cookies.remove('twitchRefreshToken');
                    }
                
                } catch(refreshTwitchTokenError: any) {
                    if(refreshTwitchTokenError.response.status === 401) {
                        Cookies.remove('accessToken');
                        Cookies.remove('refreshToken');
                        Cookies.remove('twitchAccessToken');
                        Cookies.remove('twitchRefreshToken');
                        // window.location.href = "http://127.0.0.1:5173/"
                    }
                    setError(refreshTwitchTokenError);
                }
            } else {
                setError(error);
            }
        } finally {
            setIsLoading(false);
        }
    };

    return { data, isLoading, error, fetchData, status };
};

export default useFetch;
