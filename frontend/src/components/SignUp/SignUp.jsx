import {useState} from "react";
import {Button, Form} from "react-bootstrap";
import "./SignUp.css"
import axios from "axios";
import {Redirect} from "react-router-dom";
import {useNotification} from "../partial/Notifications/NotificationProvider";

export default function SignUp() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [email, setEmail] = useState("");
    const [registered, setRegistered] = useState(false);
    const dispatch = useNotification();

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    async function handleSubmit(event) {
        event.preventDefault();

        await instance.post('registration/',
            {
                username,
                email,
                password
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Konto zostało zarejestrowane. Sprawdź swoją pocztę.",
                    title: "Success"
                })
                setRegistered(true);
            })
            .catch(err => {
                dispatch({
                    type: "ERROR",
                    message: err.message,
                    title: "Error"
                })
            })
    }

    function validateForm() {
        return username.length > 0
            && password.length > 0
            && password === repeatedPassword
            && email.length > 0;
    }

    if(registered) {
        return <Redirect to="/login"/>
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
                <Button className="mt-2" block size="lg" type="submit" disabled={!validateForm()}>
                    Zarejestruj
                </Button>
            </Form>
        </div>
    )
}