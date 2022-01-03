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

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get(`/works/${id}`)
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
                <div className="col-2">
                    Menu
                </div>
                <div className="col-md border-right">
                    <div className="p-3 py-3">
                        <div className="mb-3 align-content-md-start">
                            <h2 className="text-center">{data.title}</h2>
                            <MDEditor.Markdown
                                source={data.text}/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}