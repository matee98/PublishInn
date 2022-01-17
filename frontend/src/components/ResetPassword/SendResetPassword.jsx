import {Button, Form} from "react-bootstrap";
import {useState} from "react";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";

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
                    message: "Na podany adres e-mail została wysłana wiadomość z linkiem resetującym.",
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