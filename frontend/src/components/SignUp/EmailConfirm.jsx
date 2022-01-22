import {Link, Redirect, useParams} from "react-router-dom";
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import React, {useState} from "react";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function EmailConfirm() {
    const { token } = useParams()
    const [confirmed, setConfirmed] = useState(false)

    const dispatch = useNotification()
    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const handleConfirm = () => {
        instance.get(`/registration/confirm?token=${token}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.ACCOUNT_CONFIRMED,
                    title: "Success"
                })
                setConfirmed(true)
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    if (confirmed) {
        return <Redirect to="/login" />
    }

    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Potwierdź adres e-mail</li>
            </BreadCrumb>
            <div className="h5 pt-3">
                <p>Dziękujemy za rejestrację. Kliknij poniżej, aby potwierdzić konto:</p>
            </div>
            <Button onClick={handleConfirm}>Potwierdź</Button>
        </div>
    )
}