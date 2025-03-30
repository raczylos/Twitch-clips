import  { useState, useEffect } from 'react';
import useFetch from '../hooks/useFetch';
import TwitchLogin from './TwitchLogin';
import Cookies from 'js-cookie';
import React from 'react';


const Login = () => {
	const twitchAccessToken = Cookies.get('twitchAccessToken');
	const twitchRefreshToken = Cookies.get('twitchRefreshToken');
	const jwtToken = Cookies.get('jwtToken');

	const [loginFormData, setLoginFormData] = useState({
		username: '',
		password: '',
	});

	const { data, isLoading, error, fetchData } = useFetch();

	const handleLoginSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
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
			<div className='flex-1 flex justify-center items-center h-[calc(100vh-60px)] pb-60'>
				<div className="form-card">
					<h2 className='pb-4'>Log in</h2>
					<form onSubmit={handleLoginSubmit} className="flex flex-col gap-2">
						<label htmlFor="username">Username:</label>
						<input
							type="text"
							id="username"
							name="username"
							value={loginFormData.username}
							onChange={(event) => setLoginFormData({...loginFormData, username: event.target.value})}
							required
							placeholder="username"
							className="tw-input"
							
						/>


						<label htmlFor="password">Password:</label>
						<input
							type="password"
							id="password"
							name="password"
							value={loginFormData.password}
							onChange={(event) => setLoginFormData({...loginFormData, password: event.target.value})}
							required
							placeholder="•••••••••"
							className="tw-input"
						/>


						<button className='tw-button-primary' type="submit">Login</button>
						<p className="flex justify-center">or</p>
						<TwitchLogin />
					</form>

					
				</div>
			</div>
		</>
        
	);
}

export default Login;

