import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router} from 'react-router-dom';
import axios from "axios";
import NotificationProvider from "./components/partial/Notifications/NotificationProvider";
import CriticalOperationProvider from "./components/partial/CriticalOperations/CriticalOperationsProvider";
import "antd/dist/antd.css"

axios.defaults.baseURL = "http://localhost:8080/api/";
axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')

axios.interceptors.response.use(response => {
    return response;
}, error => {
    return new Promise((resolve, reject) => {
        const originalReq = error.config;
        if (error.response.status === 401 && error.config && !originalReq._retry) {
            originalReq._retry = true

            console.log("refreshing...")

            let res = fetch("http://localhost:8080/api/token/refresh", {
                method: "GET",
                headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('refreshToken')
                }
            }).then(res => res.json())
                .then(data => {
                localStorage.setItem('token', data.access_token);
                localStorage.setItem('refreshToken', data.refresh_token);
                axios.defaults.headers.common['Authorization'] = 'Bearer ' + data.access_token
                originalReq.headers['Authorization'] = 'Bearer ' + data.access_token;
                return axios(originalReq)
            });

            resolve(res);
        }

        return Promise.reject(error)
    })
})

ReactDOM.render(
  <React.StrictMode>
      <NotificationProvider>
          <CriticalOperationProvider>
              <Router>
                <App />
              </Router>
          </CriticalOperationProvider>
      </NotificationProvider>
  </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
