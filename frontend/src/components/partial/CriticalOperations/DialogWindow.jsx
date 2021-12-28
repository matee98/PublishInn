import React, {useState} from 'react';
import {Button, Modal} from "react-bootstrap";

function Dialog(props) {
    const [show, setShow] = useState(true);

    const {noCancel} = props

    const handleClose = () => {
        setShow(false);
        setTimeout(() => {
            props.dispatch({
                type: "REMOVE_DIALOG",
                id: props.id
            })
        }, 100);
    };

    const handleCloseCancel = () => {
        handleClose();
        props.callbackOnCancel();
    }

    const handleCloseSave = () => {
        handleClose();
        props.callbackOnSave();
    }

    return (
        <Modal show={show} onHide={handleCloseCancel} className={"text-dark"}>
            <Modal.Header closeButton>
                <Modal.Title>{props.title}</Modal.Title>
            </Modal.Header>
            <Modal.Body style={{whiteSpace: "pre-line"}}>{props.message}</Modal.Body>
            <Modal.Footer>
                {noCancel !== true &&
                <Button variant="secondary" onClick={handleCloseCancel}>
                    Anuluj
                </Button>
                }
                <Button variant="primary" onClick={handleCloseSave}>
                    Potwierdź
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default Dialog;