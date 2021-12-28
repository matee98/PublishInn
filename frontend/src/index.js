import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import reportWebVitals from './reportWebVitals';
import { BrowserRouter as Router} from 'react-router-dom';
import axios from "axios";
import NotificationProvider from "./components/partial/Notifications/NotificationProvider";
import CriticalOperationProvider from "./components/partial/CriticalOperations/CriticalOperationsProvider";

axios.defaults.baseURL = "http://localhost:8080/api/";
axios.defaults.headers.common['Authorization'] = 'Bearer ' + localStorage.getItem('token')

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
