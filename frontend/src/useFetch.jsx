import { useState } from 'react';
import axios from 'axios';
import Cookies from 'js-cookie';

const useFetch = () => {
    const [data, setData] = useState(null);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const [status, setStatus] = useState(null);
    const fetchData = async (url, method) => {
        setIsLoading(true);
        setError(null);

        const accessToken = Cookies.get('accessToken');
        let config = {};
        config.method = method;

        if (accessToken) {
            console.log("accessToken:", accessToken);
            config = {
                headers: {
                    Authorization: `Bearer ${accessToken}`
                }
            };
        }
        console.log("config", config)
        console.log("url", url)
        axios(url, config)
            .then((response) => {
                setData(response.data);
                setStatus(response.status);
                console.log("data", data); 
            })
            .catch(async (error) => {
                console.log("error", error)
                if(error.response.status === 401) {
                    try{
                        const refreshToken = Cookies.get('refreshToken');
                        config = {
                            headers: {
                                Authorization: `Bearer ${refreshToken}`
                            }
                        };
                        const response = await axios.post('http://localhost:8080/api/v1/auth/refresh-token', null, config);

                        console.log("response", response)
                        const newAccessToken = response.data.accessToken;
                        if(newAccessToken){
                            Cookies.set('accessToken', newAccessToken);
                        } else {
                            Cookies.remove('accessToken');
                            Cookies.remove('refreshToken');
                            Cookies.remove('twitchAccessToken');
                            Cookies.remove('twitchRefreshToken');
                            window.location.href = "http://127.0.0.1:5173/"
                        }

                        return fetchData(url, method);
                    } catch (refreshTokenError) {
                        if(refreshTokenError.response.status === 401) {
                            Cookies.remove('accessToken');
                            Cookies.remove('refreshToken');
                            Cookies.remove('twitchAccessToken');
                            Cookies.remove('twitchRefreshToken');
                            window.location.href = "http://127.0.0.1:5173/"
                        }
                        setError(refreshTokenError);
                    }
                    
                } else {
                    setError(error);
                }
            })
            .finally(() => {
                setIsLoading(false);
            });
    };

    return { data, isLoading, error, fetchData, status };
};

export default useFetch;
