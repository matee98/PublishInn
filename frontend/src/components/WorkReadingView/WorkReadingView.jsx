import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import MDEditor from "@uiw/react-md-editor";

export default function WorkReadingView() {
    const { id } = useParams();
    const [data, setData] = useState({
        title: "",
        author: "",
        rating: "",
        text: ""
    });

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        instance.get(`/works/${id}`)
            .then((res) => {
                setData({
                    ...res.data,
                    author: res.data.userId
                });
            })
    }

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-2 py-5">
                    Menu
                </div>
                <div className="col-md border-start border-end">
                    <div className="p-3 py-3">
                        <div className="mb-3 align-content-md-start">
                            <h2 className="text-center">{data.title}</h2>
                            <MDEditor.Markdown
                                source={data.text}
                                style={{
                                    textAlign: "left"
                                }}/>
                        </div>
                    </div>
                </div>
                <div className="col-2 py-5">
                    Autor
                </div>
            </div>
        </div>
    )
}