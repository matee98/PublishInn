import React, {useState} from "react";
import {Button, Form} from "react-bootstrap";
import "./SignUp.css"
import axios from "axios";
import {Link, Redirect} from "react-router-dom";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function SignUp() {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [email, setEmail] = useState("");
    const [registered, setRegistered] = useState(false);
    const dispatch = useNotification();

    const [usernameFocus, setUsernameFocus] = useState(false);
    const [passwordFocus, setPasswordFocus] = useState(false);

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    async function handleSubmit(event) {
        event.preventDefault();

        instance.post('registration/',
            {
                username,
                email,
                password
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.ACCOUNT_REGISTERED,
                    title: "Success"
                })
                setRegistered(true);
            })
            .catch(err => {
                if (err.response.status === 400) {
                    dispatch({
                        type: "ERROR",
                        message: ResponseMessages.BAD_DATA,
                        title: "Error"
                    })
                } else {
                    dispatch({
                        type: "ERROR",
                        message: ResponseMessages.ERROR,
                        title: "Error"
                    })
                }
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
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Rejestracja</li>
            </BreadCrumb>
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
                        {usernameFocus &&
                            <Form.Label className="info-label">
                                Nazwa użytkownika musi składać się z małych lub dużych liter oraz cyfr
                            </Form.Label>
                        }
                        <Form.Control
                            type="text"
                            value={username}
                            onChange={(event => setUsername(event.target.value))}
                            onFocus={() => setUsernameFocus(true)}
                            onBlur={() => setUsernameFocus(false)}
                        />
                    </Form.Group>
                    <Form.Group size="lg" controlId="password">
                        <Form.Label>Hasło</Form.Label>
                        {passwordFocus &&
                        <Form.Label className="info-label">
                            Hasło powinno zawierać przynajmniej jedną małą literę, jedną dużą literę, cyfrę oraz znak
                            specjalny. Musi mieć co najmniej 8 znaków długości.
                        </Form.Label>
                        }
                        <Form.Control
                            type="password"
                            value={password}
                            onChange={(event => setPassword(event.target.value))}
                            onFocus={() => setPasswordFocus(true)}
                            onBlur={() => setPasswordFocus(false)}
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
        </div>
    )
}