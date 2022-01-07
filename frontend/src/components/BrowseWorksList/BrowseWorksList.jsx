import {useEffect, useState} from "react";
import axios from "axios";
import {Card, Pagination} from "antd";
import CategoryMenu from "../partial/CategoryMenu";
import {Button} from "react-bootstrap";
import {dateConverter} from "../helpers/DateConverter";
import {Link, useParams} from "react-router-dom";
import WorksList from "../partial/WorksList/WorksList";


export default function BrowseWorksList() {

    const {type} = useParams()
    const [loading, setLoading] = useState(true)

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
        instance.get(`/works?type=${type}`)
            .then((res) => {
                setData(res.data)
            })
    }

    useEffect(() => {
        fetchData()
    }, [type])

    useEffect(() => {
        if (data[0] !== undefined) {
            if (data[0].title !== "") {
                setLoading(false)
            }
        }
    }, [data])

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-2 py-5">
                    <CategoryMenu />
                </div>
                <div className="col-md border-start border-end">
                    <WorksList
                        data={data}
                        loading={loading}
                        numberEachpage={3} />
                </div>
            </div>
        </div>
    )
}