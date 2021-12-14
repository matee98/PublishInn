import React, {useEffect, useState} from "react";
import DataTable from 'react-data-table-component';
import {Button, Col, Container, Row} from "react-bootstrap";
import axios from "axios";

function UsersAccountsList(props) {
    const [data, setData] = useState([
        {
            username: "",
            email: "",
            enabled: true,
            locked: false
        }
    ])

    const columns = [
        {
            name: 'Login',
            selector: 'username',
            sortable: true,
        },
        {
            name: 'E-mail',
            selector: 'email',
            sortable: true,
        },
        {
            name: 'Potwierdzone',
            selector: 'enabled',
            cell: row => {
                return (
                    <label className="labels float-end">{row.enabled ? "TAK" : "NIE"}</label>
                )
            },
        },
        {
            name: 'Aktywne',
            selector: 'locked',
            cell: row => {
                return (
                    <label className="labels float-end">{row.locked ? "NIE" : "TAK"}</label>
                )
            },
        },
        {
            name: "",
            selector: 'details',
            cell: row => {
                return (
                    <Button className="btn-sm">Szczegóły</Button>
                )
            }
        }
    ]

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = () => {
        axios.get("/users")
            .then(res => {
                console.log(res);
                setData(res.data);
            })
    }

    return (
        <div className="container-fluid">
            <Container>
                <Row>
                    <Col xs={12} sm={12} md={12} lg={12} xl={12} className={"floating-no-absolute py-4 mx-auto mb-2"}>
                        <div>
                            <div>
                                <h1 className="float-left">Lista użytkowników</h1>
                                <Button className="btn-secondary float-right m-2" onClick={() => {
                                    fetchData()
                                }}>Odśwież</Button>
                            </div>
                            <DataTable className={"rounded-0"}
                                       noDataComponent="brak wyników"
                                       columns={columns}
                                       data={data}
                                       subHeader
                                       noHeader={true}
                            />
                        </div>
                    </Col>
                </Row>
            </Container>
        </div>
    )
}

export default UsersAccountsList