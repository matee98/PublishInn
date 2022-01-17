import {Redirect, useParams} from "react-router-dom";
import {Button, Form} from "react-bootstrap";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {useState} from "react";

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
                    message: "Konto zostało potwierdzone. Możesz się zalogować.",
                    title: "Success"
                })
                setConfirmed(true)
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: "Wystąpił bład. Spróbuj ponownie później",
                    title: "Error"
                })
            })
    }

    if (confirmed) {
        return <Redirect to="/login" />
    }

    return (
        <div className="container-fluid">
            <div className="h5 pt-3">
                <p>Dziękujemy za rejestrację. Kliknij poniżej, aby potwierdzić konto:</p>
            </div>
            <Button onClick={handleConfirm}>Potwierdź</Button>
        </div>
    )
}