import React from 'react';
import { Navigate, Outlet  } from 'react-router-dom';
import Cookies from 'js-cookie';

const ProtectedRoute = () => {
    const isLoggedIn = () => {
        const accessToken = Cookies.get('accessToken')
        return !!accessToken
    };

    console.log(isLoggedIn())

    return (isLoggedIn() ? <Outlet /> : <Navigate to="/login" />)
};

export default ProtectedRoute;