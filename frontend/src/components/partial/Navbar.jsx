import {Component} from "react";
import {Button, Container, Form, FormControl, Nav, Navbar, NavDropdown} from "react-bootstrap";
import {Link} from "react-router-dom";
import logo from '../../assets/logo.png'

class NavigationBar extends Component {

    handleRefresh = () => {
        window.location.reload();
    }

    handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('username');
        localStorage.removeItem('userRole');
        this.handleRefresh();
    }

    render() {
        let authPanel;
        if (localStorage.getItem('username')) {
            authPanel =
            <div>
                <Link to="/account/info" className="d-inline-block p-1">
                    <Button variant="outline-success">Moje konto</Button>
                </Link>
                <Link to="/" className="d-inline-block p-1">
                    <Button variant="outline-success" onClick={this.handleLogout}>Wyloguj</Button>
                </Link>
            </div>
        } else {
            authPanel =
        <div>
            <Link to="/login" className="d-inline-block p-1">
                <Button variant="outline-success">Zaloguj</Button>
            </Link>
            <Link to="/register" className="d-inline-block p-1">
                <Button variant="outline-success">Zarejestruj</Button>
            </Link>
        </div>
        }

        return(
            <Navbar bg="light" expand="lg" className="border-bottom border-secondary">
                <Container fluid>
                    <Navbar.Brand>
                        <Link to = "/">
                            <img src={logo} alt="Logo"/>
                        </Link>
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarScroll" />
                    <Navbar.Collapse id="navbarScroll">
                        <Nav
                            className="me-auto my-2 my-lg-0"
                            style={{ maxHeight: '100px' }}
                            navbarScroll
                        >
                            <Nav.Link>
                                <Link to="/" className="text-decoration-none text-secondary">Home</Link>
                            </Nav.Link>
                            <Nav.Link href="#action2">Link</Nav.Link>
                            <NavDropdown title="Link" id="navbarScrollingDropdown">
                                <NavDropdown.Item href="#action3">Action</NavDropdown.Item>
                                <NavDropdown.Item href="#action4">Another action</NavDropdown.Item>
                                <NavDropdown.Divider />
                                <NavDropdown.Item href="#action5">
                                    Something else here
                                </NavDropdown.Item>
                            </NavDropdown>
                            <Nav.Link href="#" disabled>
                                Link
                            </Nav.Link>
                            {localStorage.getItem('userRole') === 'ADMIN' &&
                            <Nav.Link>
                                <Link to="/accounts" className="text-decoration-none text-secondary">
                                    Zarządzaj użytkownikami
                                </Link>
                            </Nav.Link>
                            }
                            <Form className="d-flex mx-3">
                                <FormControl
                                    type="search"
                                    placeholder="Szukaj..."
                                    className="me-2"
                                    aria-label="Search"
                                />
                                <Button variant="outline-success">Szukaj</Button>
                            </Form>
                        </Nav>
                        {authPanel}
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        )
    }
}

export default (NavigationBar);