import React from "react";
import {Nav} from "react-bootstrap";
import "./NotFound.css";
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";

export default function NotFound() {
    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">404</li>
            </BreadCrumb>
            <div className="NotFound text-center">
                <h2>Coś poszło nie tak, nie udało nam się odnaleźć strony :(</h2>
            </div>
            <div>
                <h4>
                    <Nav.Link href="/">Powrót do strony głównej</Nav.Link>
                </h4>
            </div>
        </div>
    );
}