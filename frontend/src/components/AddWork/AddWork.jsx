import {Button, Form, FormCheck} from "react-bootstrap";
import React, {useState} from "react";
import MDEditor from '@uiw/react-md-editor';
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {useDialogPermanentChange} from "../partial/CriticalOperations/CriticalOperationsProvider";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function AddWork() {
    const [title, setTitle] = useState("");
    const [category, setCategory] = useState("");
    const [genre, setGenre] = useState("");
    const [content, setContent] = useState("");
    const [comDis, setComDis] = useState(false);

    const dispatch = useNotification();
    const dispatchDialog = useDialogPermanentChange();

    const workTypes = [
        "CRIME",
        "DRAMA",
        "FANTASY",
        "HORROR",
        "OTHER",
        "POEM",
        "SCI_FI",
        "THRILLER"
    ]

    const handleCategoryChoice = (choice) => {
        setCategory(choice);
        if (choice === "poetry") {
            setGenre("POEM")
        }
    }

    const handleSubmit = () => {
        axios.post("/works", {
            title: title,
            type: genre,
            text: content
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.CHANGES_SAVED,
                    title: "Success"
                })
            })
            .catch(err => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    return(
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Dodaj utwór</li>
            </BreadCrumb>
            <Form.Group size="lg" controlId="title">
                <Form.Control
                    autoFocus
                    type="text"
                    value={title}
                    placeholder='Tytuł'
                    className="mb-2"
                    onChange={(event => setTitle(event.target.value))}
                />
                <Form.Select value={category}
                             onChange={(e) => handleCategoryChoice(e.target.value)}
                             className="mb-2">
                    <option value="">Wybierz rodzaj</option>
                    <option value="prose">Proza</option>
                    <option value="poetry">Poezja</option>
                </Form.Select>
                {category === "prose" &&
                    <Form.Select value={genre} onChange={(e) => setGenre(e.target.value)}>
                        <option value="">Wybierz gatunek</option>
                        <option value={workTypes[2]}>Fantasy</option>
                        <option value={workTypes[3]}>Horror</option>
                        <option value={workTypes[4]}>Inny</option>
                        <option value={workTypes[0]}>Kryminał</option>
                        <option value={workTypes[1]}>Obyczajowy</option>
                        <option value={workTypes[6]}>Science-fiction</option>
                        <option value={workTypes[7]}>Thriller</option>
                    </Form.Select>}
                <MDEditor
                    placeholder="Wpisz tekst..."
                    className="mt-2 mb-2"
                    value={content}
                    onChange={setContent}
                    height="550"
                />
            </Form.Group>
            <FormCheck className="mb-2">
                <input
                    className="form-check-input mx-1"
                    type="checkbox"
                    id="comDisCheck"
                    value={comDis}
                    onChange={() => setComDis(!comDis)}/>
                <label
                    className="form-check-label"
                    for="comDisCheck">
                    Wyłącz możliwość komentowania
                </label>
            </FormCheck>
            <Button
                onClick={() => dispatchDialog({
                    callbackOnSave:() => {
                        handleSubmit()
                    }
                })}>Zapisz</Button>
        </div>
    )
}