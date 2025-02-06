// import axios from 'axios';
// import Cookies from 'js-cookie';


// axios.interceptors.response.use(
//     (response) => response,
//     async (error) => {
//       const originalRequest = error.config;
  
//       if (error.response.status === 401 && !originalRequest._retry) {
//         originalRequest._retry = true;
  
//         try {
//           const refreshToken = Cookies.get('refreshToken');
//           const response = await axios.post('/api/refresh-token', { refreshToken });
//           const { token } = response.data;
  
//           Cookies.set('jwtToken', token);
  
//           // Retry the original request with the new token
//           originalRequest.headers.Authorization = `Bearer ${token}`;
//           return axios(originalRequest);
//         } catch (error) {
//           // Handle refresh token error or redirect to login
//         }
//       }
  
//       return Promise.reject(error);
//     }
//   );
  