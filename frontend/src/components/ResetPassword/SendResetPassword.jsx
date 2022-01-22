import {Button, Form} from "react-bootstrap";
import React, {useState} from "react";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function SendResetPassword() {
    const [email, setEmail] = useState("");

    function validateForm() {
        return email.length > 0;
    }

    const dispatch = useNotification();

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const handleSubmit = () => {
        instance.post("/account/password/reset", {
            email: email
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.PASS_CODE_SENT,
                    title: "Success"
                })
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    return (
        <div className="container-fluid" style={{
            maxWidth: "320px"
        }}>
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Resetuj hasło</li>
            </BreadCrumb>
            <div className="h3 mb-3 pt-3">Resetowanie hasła</div>
            <Form>
                <Form.Group size="lg" controlId="email">
                    <Form.Label>Adres e-mail</Form.Label>
                    <Form.Control
                        autoFocus
                        type="email"
                        value={email}
                        onChange={(event => setEmail(event.target.value))}
                    />
                </Form.Group>
                <Button onClick={handleSubmit} block size="lg" className="mt-2" disabled={!validateForm()}>
                    Potwierdź
                </Button>
            </Form>
        </div>
    )
}