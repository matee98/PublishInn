import {useState} from "react";
import {Button, Form} from "react-bootstrap";
import "./SignUp.css"

export default function SignUp() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [email, setEmail] = useState("");

    function handleSubmit(event) {
        event.preventDefault();
    }

    function validateForm() {
        return username.length > 0
            && password.length > 0
            && password === repeatedPassword
            && email.length > 0;
    }

    return (
        <div className="SignUp">
            <Form onSubmit={handleSubmit}>
                <Form.Group size="lg" controlId="emailAddress">
                    <Form.Label>Adres e-mail</Form.Label>
                    <Form.Control
                        autoFocus
                        type="email"
                        value={email}
                        onChange={(event => setEmail(event.target.value))}
                    />
                </Form.Group>
                <Form.Group size="lg" controlId="username">
                    <Form.Label>Nazwa użytkownika</Form.Label>
                    <Form.Control
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
                <Form.Group size="lg" controlId="repeatedPassword">
                    <Form.Label>Powtórz hasło</Form.Label>
                    <Form.Control
                        type="password"
                        value={repeatedPassword}
                        onChange={(event => setRepeatedPassword(event.target.value))}
                    />
                </Form.Group>
                <Button block size="lg" type="submit" disabled={!validateForm()}>
                    Zarejestruj
                </Button>
            </Form>
        </div>
    )
}