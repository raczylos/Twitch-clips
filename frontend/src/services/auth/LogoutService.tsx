import Cookies from 'js-cookie';

export const handleLogout = (status) => {
    try {

        console.log("status", status)


        if (status === 200) {
            Cookies.remove('accessToken');
            Cookies.remove('refreshToken');
            Cookies.remove('twitchAccessToken');
            Cookies.remove('twitchRefreshToken');

            console.log("Logged out successfully");

            window.location.href = "http://127.0.0.1:5173/";
        } else {
            console.error('Logout failed with status:', status);
        }
    } catch (error) {
        console.error('Error:', error);
    }
};
