import {Link, useParams} from "react-router-dom";
import BreadCrumb from "../partial/Breadcrumb";
import CategoryMenu from "../partial/CategoryMenu";
import WorksList from "../partial/WorksList/WorksList";
import React, {useEffect, useState} from "react";
import axios from "axios";

export default function SearchResults() {
    const {query} = useParams();
    const [loading, setLoading] = useState(true);
    const [data, setData] = useState([{
        id: "",
        title: "",
        username: "",
        type: "",
        rating: "",
        createdOn: ""
    }])

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const fetchData = () => {
        instance.get(`/works/search?name=${query}`)
            .then((res) => {
                setData(res.data)
                setLoading(false)
            })
    }

    useEffect(() => {
        fetchData()
    }, [])

    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Szukaj</li>
            </BreadCrumb>
            <div className="row">
                <div className="col-2 py-5">
                    <CategoryMenu />
                </div>
                <div className="col-md border-start border-end">
                    <p className="h3">Wyniki wyszukiwania:</p>
                    <WorksList
                        data={data}
                        loading={loading}
                        numberEachpage={5} />
                </div>
            </div>
        </div>
    )
}