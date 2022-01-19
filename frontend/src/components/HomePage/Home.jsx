import React, {Component} from "react";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";

export default class Home extends Component{
    render(){
        if (localStorage.getItem('username')){
            return (
                <div className="Home">
                    <div className="lander">
                        <h1>Witaj, {localStorage.getItem('username')}</h1>
                        <Button>
                            <Link to='/test/new'>
                                Dodaj pracę
                            </Link>
                        </Button>
                    </div>
                </div>
            );
        }
        return (
            <div className="Home">
                <div className="container-fluid">
                    <header className="py-5">
                        <div className="container px-lg-5">
                            <div className="p-4 p-lg-5 bg-light rounded-3 text-center">
                                <div className="m-4 m-lg-5">
                                    <h1 className="display-5 fw-bold">Cześć!</h1>
                                    <p className="fs-4">PublishInn to aplikacja internetowa zrealizowana w ramach
                                    pracy inżynierskiej na Politechnice Łódzkiej. Służy do dzielenia się swoją
                                    twórczością literacką z innymi użytkownikami.</p>
                                    <Link to="/register">
                                        <Button>
                                            Dołącz do nas!
                                        </Button>
                                    </Link>
                                </div>
                            </div>
                        </div>
                    </header>
                    <section className="pt-4">
                        <div className="container px-lg-5">
                            <div className="row gx-lg-5">
                                <div className="col-lg-6 col-xxl-4 mb-5">
                                    <div className="card bg-light border-0 h-100">
                                        <div className="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                            <div
                                                className="feature bg-opacity-75 bg-secondary bg-gradient rounded-3 mb-3 mt-n4">
                                                <i className="bi bi-book" style={{
                                                    fontSize: "2rem"
                                                }}/>
                                            </div>
                                            <h2 className="fs-4 fw-bold">Publikuj</h2>
                                            <p className="mb-0">Dodawaj własną twórczość.</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-6 col-xxl-4 mb-5">
                                    <div className="card bg-light border-0 h-100">
                                        <div className="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                            <div
                                                className="feature bg-opacity-75 bg-secondary bg-gradient rounded-3 mb-3 mt-n4">
                                                <i className="bi bi-star-half" style={{
                                                    fontSize: "2rem"
                                                }}/>
                                            </div>
                                            <h2 className="fs-4 fw-bold">Oceniaj</h2>
                                            <p className="mb-0">Dziel się opiniami na temat utworów innych użytkowników.</p>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-lg-6 col-xxl-4 mb-5">
                                    <div className="card bg-light border-0 h-100">
                                        <div className="card-body text-center p-4 p-lg-5 pt-0 pt-lg-0">
                                            <div
                                                className="feature bg-opacity-75 bg-secondary bg-gradient rounded-3 mb-3 mt-n4">
                                                <i className="bi bi-cloud-download" style={{
                                                    fontSize: "2rem"
                                                }}/>
                                            </div>
                                            <h2 className="fs-4 fw-bold">Pobieraj</h2>
                                            <p className="mb-0">Jeśli chcesz pobierz wybrany utwór, aby przeczytać go
                                            w dowolnym momencie!</p>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        );
    }
}