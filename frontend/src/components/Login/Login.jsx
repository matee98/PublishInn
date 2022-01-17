import {useState} from "react";
import {Button, Form} from "react-bootstrap";
import axios from 'axios';
import "./Login.css"
import {Link, useHistory} from "react-router-dom";
import jwt from 'jwt-decode';
import {useNotification} from "../partial/Notifications/NotificationProvider";

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const history = useHistory();
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
                handleRefresh();
                history.push("/");
                dispatch({
                    type: "SUCCESS",
                    message: "Zalogowano pomyślnie",
                    title: "Success"
                })
            })
            .catch(err => {
                if (err.response) {
                    dispatch({
                        type: "ERROR",
                        message: "Dane logowania są niepoprawne lub konto nie jest aktywne",
                        title: "Error"
                    });
                } else if (err.request) {
                    dispatch({
                        type: "ERROR",
                        message: err.message,
                        title: "Error"
                    })
                }
            })
    }

    function validateForm() {
        return username.length > 0 && password.length > 0;
    }

    return (
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
    )
}

export default Login;