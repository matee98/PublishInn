import {useState} from "react";
import {Button, Form, Nav} from "react-bootstrap";
import axios from 'axios';
import "./Login.css"

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(event) {
        event.preventDefault();

        const params = new URLSearchParams()
        params.append('username', username)
        params.append('password', password)

        const config = {
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            }
        }

        axios.post('login', params, config)
            .then(r => {
                localStorage.setItem('token', r.data.access_token);
                localStorage.setItem('refreshToken', r.data.refresh_token);
            })
            .catch(err => {
                console.log(err);
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
                <Button block size="lg" type="submit" disabled={!validateForm()}>
                    Zaloguj
                </Button>
                <p>
                    Nie masz konta?
                    <Nav.Link href="/register" className="d-inline-block">Zarejestruj się</Nav.Link>
                </p>
            </Form>
        </div>
    )
}

export default Login;