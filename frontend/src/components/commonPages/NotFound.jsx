import React from "react";
import {Nav} from "react-bootstrap";
import "./NotFound.css";

export default function NotFound() {
    return (
        <div>
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