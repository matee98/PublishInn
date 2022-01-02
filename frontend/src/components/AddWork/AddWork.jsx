import {Button, Form} from "react-bootstrap";
import React, {useState} from "react";
import MDEditor from '@uiw/react-md-editor';
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";

export default function AddWork() {
    const [title, setTitle] = useState("");
    const [category, setCategory] = useState()
    const [content, setContent] = useState("Wpisz tekst...")

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
                <Form.Select>
                    <option>Wybierz rodzaj</option>
                    <option value="prose">Proza</option>
                    <option value="poetry">Poezja</option>
                </Form.Select>
                <MDEditor
                    className="mt-2"
                    value={content}
                    onChange={setContent}
                />
                <MDEditor.Markdown source={content} />
            </Form.Group>
            <Button onClick={() => console.log(content)}>Zapisz</Button>
        </div>
    )
}