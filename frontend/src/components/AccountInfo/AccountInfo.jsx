import React, {useEffect, useState} from "react";
import axios from "axios";
import {Button, Form} from "react-bootstrap";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";
import {useDialogPermanentChange} from "../partial/CriticalOperations/CriticalOperationsProvider";

export default function AccountInfo() {
    const [data, setData] = useState({
        username: "",
        mailAddress: "",
        userRole: "",
        enabled: false,
        locked: false
    });

    const [editable, setEditable] = useState(false);
    const [editablePart, setEditablePart] = useState("")

    const [oldPassword, setOldPassword] = useState("");
    const [password, setPassword] = useState("");
    const [repeatedPassword, setRepeatedPassword] = useState("");
    const [newEmail, setNewEmail] = useState("");

    const dispatch = useNotification();
    const dispatchDialog = useDialogPermanentChange();

    function validatePasswordForm() {
        return oldPassword.length > 0
            && password.length > 0
            && password === repeatedPassword;
    }

    function validateEmailForm() {
        return newEmail.length > 0;
    }

    const handlePasswordChange = () => {
        axios.put("/account/password/change", {
            oldPassword: oldPassword,
            newPassword: password
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.CHANGES_SAVED,
                    title: "Success"
                })
                setEditablePart("")
                setEditable(false)
                setOldPassword('')
                setPassword('')
                setRepeatedPassword('')
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    const handleEmailChange = () => {
        axios.put("/account/email/change", {
            email: newEmail
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.CHANGES_SAVED,
                    title: "Success"
                })
                setEditablePart("")
                setEditable(false)
                setNewEmail("")
            })
            .catch((err) => {
                if (err.response.status === 409) {
                    dispatch({
                        type: "ERROR",
                        message: ResponseMessages.EMAIL_EXISTS,
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

    useEffect(() => {
        axios.get("/account/info")
            .then((res) => {
                setData({
                    ...res.data,
                    mailAddress: res.data.email,
                    userRole: res.data.appUserRole,
                });
            })
            .catch(err => {
                console.log(err.response)
            })
    }, [])

    return(
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Moje konto</li>
            </BreadCrumb>
            <div className="row">
                <div className="col-md-3 border-right">
                    <div className="d-flex flex-column align-items-center text-center p-3 py-5"><img className="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg" /><span class="font-weight-bold">{data.username}</span><span> </span></div>
                </div>
                <div className="col-md-5 border-right">
                    <div className="p-3 py-5">
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h4 className="text-right">Informacje o koncie</h4>
                        </div>
                        <div className="row mt-3">
                            <div className="flex-md-row-reverse"><label className="labels float-start">Adres e-mail</label><label className="labels float-end">{data.mailAddress}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Rola użytkownika</label><label className="labels float-end">{data.userRole}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Konto potwierdzone</label><label className="labels float-end">{data.enabled ? "TAK" : "NIE"}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Konto zablokowane</label><label className="labels float-end">{data.locked ? "TAK" : "NIE"}</label></div>
                        </div>
                        {!editable &&
                            <div className="mt-5 text-center">
                                <button onClick={() => {setEditable(true)}} className="btn btn-primary profile-button" type="button">Edytuj profil</button>
                            </div>
                        }
                        {editable &&
                        <div>
                            <div className="row mt-3">
                                <p className="col-4 text-primary border-primary border rounded-1"
                                   onClick={() => {setEditablePart("password")}}>
                                    Hasło</p>
                                <p className="col-4 text-primary border-primary border rounded-1"
                                   onClick={() => {setEditablePart("email")}}>
                                    Email</p>
                            </div>
                            {editablePart === "password" &&
                            <Form>
                                <Form.Group size="lg" controlId="oldPassword">
                                    <Form.Label>Stare hasło</Form.Label>
                                    <Form.Control
                                        type="password"
                                        value={oldPassword}
                                        onChange={(event => setOldPassword(event.target.value))}
                                    />
                                </Form.Group>
                                <Form.Group size="lg" controlId="password">
                                    <Form.Label>Nowe hasło</Form.Label>
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
                                <Button onClick={() => {
                                    dispatchDialog({
                                        callbackOnSave: () => {
                                            handlePasswordChange()
                                        }
                                    })
                                }} className="mt-2" block size="lg" disabled={!validatePasswordForm()}>
                                    Potwierdź
                                </Button>
                            </Form>
                            }
                            {editablePart === "email" &&
                            <Form>
                                <Form.Group size="lg" controlId="email">
                                    <Form.Label>Nowy adres e-mail</Form.Label>
                                    <Form.Control
                                        type="email"
                                        value={newEmail}
                                        onChange={(event => setNewEmail(event.target.value))}
                                    />
                                </Form.Group>
                                <Button onClick={() => {
                                    dispatchDialog({
                                        callbackOnSave: () => {
                                            handleEmailChange()
                                        }
                                    })
                                }} className="mt-2" block size="lg" disabled={!validateEmailForm()}>
                                    Potwierdź
                                </Button>
                            </Form>
                            }
                        </div>
                        }
                    </div>
                </div>
            </div>
        </div>
    )
}