import {useState} from "react";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import axios from "axios";
import {Button, Form} from "react-bootstrap";
import {useParams} from "react-router-dom";

export default function ResetPasswordConfirm() {

    const { code } = useParams();
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");

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
                    message: "Hasło zostało zmienione.",
                    title: "Success"
                })
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: "Coś poszło nie tak. Spróbuj ponownie później.",
                    title: "Error"
                })
            })
    }

    return (
        <div className="container-fluid" style={{
            maxWidth: "320px"
        }}>
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