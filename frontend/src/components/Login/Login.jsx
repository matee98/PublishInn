import {useState} from "react";
import {Button, Form} from "react-bootstrap";
import "./Login.css"

function Login() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
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
                        autoFocus
                        type="password"
                        value={password}
                        onChange={(event => setPassword(event.target.value))}
                    />
                </Form.Group>
                <Button block size="lg" type="submit" disabled={!validateForm()}>
                    Zaloguj
                </Button>
            </Form>
        </div>
    )
}

export default Login;