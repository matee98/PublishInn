import React, {useState} from "react";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import axios from "axios";
import {Button, Form} from "react-bootstrap";
import {Link, Redirect, useParams} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function ResetPasswordConfirm() {

    const { code } = useParams();
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [changed, setChanged] = useState(false);

    function validateForm() {
        return password.length > 0
            && password === repeatedPassword
    }

    const dispatch = useNotification();

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const handleSubmit = () => {
        instance.post("/account/password/reset/confirm", {
            code: code,
            newPassword: password
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.PASSWORD_CHANGED,
                    title: "Success"
                })
                setChanged(true);
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    if (changed) {
        return <Redirect to="/login" />
    }

    return (
        <div className="container-fluid" style={{
            maxWidth: "320px"
        }}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Potwierdź reset hasła</li>
            </BreadCrumb>
            <div className="h3 mb-3 pt-3">Zmiana hasła</div>
            <Form>
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
                <Button onClick={handleSubmit} block size="lg" className="mt-2" disabled={!validateForm()}>
                    Potwierdź
                </Button>
            </Form>
        </div>
    )
}