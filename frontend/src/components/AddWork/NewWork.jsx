import React, {useCallback, useMemo, useState} from "react";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {useDialogPermanentChange} from "../partial/CriticalOperations/CriticalOperationsProvider";
import axios from "axios";
import BreadCrumb from "../partial/Breadcrumb";
import {Link} from "react-router-dom";
import './NewWork.css'
import {Button, Form, FormCheck} from "react-bootstrap";
import { createEditor, Node } from "slate";
import {Slate, Editable, withReact} from "slate-react";

export default function NewWork() {
    const [title, setTitle] = useState("");
    const [category, setCategory] = useState("");
    const [genre, setGenre] = useState("");
    const [comDis, setComDis] = useState(false);

    const serialize = value => {
        return (
            value
                .map(n => Node.string(n))
                .join('\n')
        )
    }

    const editor = useMemo(() => withReact(createEditor()), [])
    const renderElement = useCallback(({ attributes, children, element }) => {
        return <p
            className="text-area"
            {...attributes}
            >
            {children}
        </p>
    }, [])
    const [value, setValue] = useState(
        [
            {
                type: "paragraph",
                children: [{ text: ''}],
            }
        ]
    );

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
            text: serialize(value)
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Zmiany zostały zapisane",
                    title: "Success"
                })
            })
            .catch(err => {
                dispatch({
                    type: "ERROR",
                    message: err.message,
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
                <div
                    className="rounded"
                    style={{
                        border: "1px solid",
                        borderColor: "#ced4da",
                        minHeight: "550px",
                        marginTop: "8px"
                    }}>
                    <Slate
                        editor={editor}
                        value={value}
                        onChange={(newValue) => {
                            setValue(newValue)
                        }}>
                        <Editable
                            renderElement={renderElement}
                            placeholder="Tekst..."
                        />
                    </Slate>
                </div>
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