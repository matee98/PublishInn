import React, {useState} from "react";
import {Button, Form} from "react-bootstrap";
import axios from 'axios';
import "./Login.css"
import {Link} from "react-router-dom";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const dispatch = useNotification();

    const handleRefresh = () => {
        window.location.reload();
    }

    async function handleSubmit(event) {
        event.preventDefault();

        const params = new URLSearchParams()
        params.append('username', username)
        params.append('password', password)

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }

        await axios.post('login', params, config)
            .then((res) => {
                localStorage.setItem('token', res.data.access_token)
                localStorage.setItem('refreshToken', res.data.refresh_token)
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.LOGIN_SUCCESS,
                    title: "Success"
                })
                handleRefresh();
            })
            .catch((err) => {
                if (err.response.status === 403) {
                    dispatch({
                        type: "ERROR",
                        message: ResponseMessages.LOGIN_FAILED,
                        title: "Error"
                    })
                }
            })
    }

    function validateForm() {
        return username.length > 0 && password.length > 0;
    }

    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Logowanie</li>
            </BreadCrumb>
            <div className="Login">
                <Form onSubmit={handleSubmit}>
                    <Form.Group size="lg" controlId="username">
                        <Form.Label>Nazwa użytkownika</Form.Label>
                        <Form.Control
                            autoFocus
                            type="text"
                            value={username}
                            onChange={(event => setUsername(event.target.value))}
                            />
                    </Form.Group>
                    <Form.Group size="lg" controlId="password">
                        <Form.Label>Hasło</Form.Label>
                        <Form.Control
                            type="password"
                            value={password}
                            onChange={(event => setPassword(event.target.value))}
                        />
                    </Form.Group>
                    <Button block size="lg" type="submit" className="mt-2" disabled={!validateForm()}>
                        Zaloguj
                    </Button>
                    <p>
                        Nie masz konta?
                        <Link to="/register" className="d-inline-block mx-1">Zarejestruj się</Link>
                    </p>
                    <p>
                        <Link to="/password/reset">Resetowanie hasła</Link>
                    </p>
                </Form>
            </div>
        </div>
    )
}

export default Login;