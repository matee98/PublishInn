import {Component} from "react";
import {Button, Container, Form, FormControl, Nav, Navbar, NavDropdown} from "react-bootstrap";

class NavigationBar extends Component {
    render() {
        return(
            <Navbar bg="light" expand="lg">
                <Container fluid>
                    <Navbar.Brand href="/">Navbar scroll</Navbar.Brand>
                    <Navbar.Toggle aria-controls="navbarScroll" />
                    <Navbar.Collapse id="navbarScroll">
                        <Nav
                            className="me-auto my-2 my-lg-0"
                            style={{ maxHeight: '100px' }}
                            navbarScroll
                        >
                            <Nav.Link href="#action1">Home</Nav.Link>
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
                        <div>
                            <Nav.Link href="/login" className="d-inline-block p-1">
                                <Button variant="outline-success">Zaloguj</Button>
                            </Nav.Link>
                            <Nav.Link href="/register" className="d-inline-block p-1">
                                <Button variant="outline-success">Zarejestruj</Button>
                            </Nav.Link>
                        </div>
                    </Navbar.Collapse>
                </Container>
            </Navbar>
        )
    }
}

export default (NavigationBar);