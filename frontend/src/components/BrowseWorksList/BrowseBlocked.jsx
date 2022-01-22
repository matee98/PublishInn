import React, {useEffect, useState} from "react";
import axios from "axios";
import WorksList from "../partial/WorksList/WorksList";
import {Link} from "react-router-dom";

export default function BrowseBlocked() {
    const [loading, setLoading] = useState(true)

    const [data, setData] = useState([{
        id: "",
        title: "",
        username: "",
        type: "",
        rating: "",
        createdOn: "",
        status: ""
    }])

    const fetchData = () => {
        axios.get(`/works/moderator/blocked`)
            .then((res) => {
                setData(res.data)
            })
    }

    useEffect(() => {
        fetchData()
    }, [])

    useEffect(() => {
        if (data[0] !== undefined) {
            if (data[0].title !== "") {
                setLoading(false)
            }
        }
    }, [data])

    return (
        <div className="container-fluid">
            <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
            <li className="breadcrumb-item active">Zablokowane utwory</li>
            <div className="row">
                <div className="col-2 py-5">
                </div>
                <div className="col-md border-start border-end">
                    <WorksList
                        data={data}
                        loading={loading}
                        numberEachpage={5} />
                </div>
            </div>
        </div>
    )
}