import BreadCrumb from "../partial/Breadcrumb";
import {Link} from "react-router-dom";
import {Nav} from "react-bootstrap";
import React from "react";

export default function Forbidden() {
    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">403</li>
            </BreadCrumb>
            <div className="text-center">
                <h2>Brak dostępu do zasobu</h2>
            </div>
            <div>
                <h4>
                    <Nav.Link href="/">Powrót do strony głównej</Nav.Link>
                </h4>
            </div>
        </div>
    );
}