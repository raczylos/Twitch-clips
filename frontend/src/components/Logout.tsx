
import useFetch from '../hooks/useFetch';
import {  useEffect } from "react";
import { handleLogout } from '../services/auth/LogoutService';
import React from 'react';

const Logout = () => {

    const {fetchData, status} = useFetch();

    const onLogoutClick = async() => {
        await fetchData(`http://localhost:8080/api/v1/auth/logout`, 'POST');
        console.log("lolz", status)

1
    };

    useEffect(() => {
        if (status !== null) {
            console.log("Status:", status);
            handleLogout(status); 
        }
    }, [status]);


    return (
        <button onClick={onLogoutClick}>
            Log Out
        </button>
    );
};

export default Logout;