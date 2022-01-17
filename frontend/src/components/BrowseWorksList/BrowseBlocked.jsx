import {useEffect, useState} from "react";
import axios from "axios";
import WorksList from "../partial/WorksList/WorksList";
import CategoryMenu from "../partial/CategoryMenu";

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