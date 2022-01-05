import {useEffect, useState} from "react";
import axios from "axios";
import {Card, Pagination} from "antd";
import CategoryMenu from "../partial/CategoryMenu";
import {Button} from "react-bootstrap";
import {dateConverter} from "../helpers/DateConverter";
import {Link} from "react-router-dom";


export default function BrowseWorksList(props) {

    const genre = props.genre
    const numEachPage = 3
    const [minValue, setMinValue] = useState(0)
    const [maxValue, setMaxValue] = useState(numEachPage)
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
        instance.get('/works')
            .then((res) => {
                setData(res.data)
            })
    }

    useEffect(() => {
        fetchData()
    }, [])

    useEffect(() => {
        if (data[0].title !== "") {
            setLoading(false)
        }
    }, [data])

    const handleChange = (value) => {
        setMinValue((value - 1) * numEachPage)
        setMaxValue(value * numEachPage)
    }

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-2 py-5">
                    <CategoryMenu />
                </div>
                <div className="col-md border-start border-end">
                    {
                        data &&
                            data.length > 0 &&
                            data.slice(minValue, maxValue).map((value => (
                                <Card
                                    title={value.title}
                                    extra={
                                        <Link to={`/works/${value.id}`}>
                                            <Button>
                                                Czytaj >>>
                                            </Button>
                                        </Link>
                                    }
                                    style={{
                                        width: "auto"
                                    }}
                                    loading={loading}
                                >
                                    <p className="text-start">
                                        Autor: <span><Link to={`/users/profile/${value.username}`}>{value.username}</Link></span>
                                    </p>
                                    <p className="text-start">Ocena: {value.rating !== null ? value.rating : "brak"}</p>
                                    <p className="text-start">Dodano: {dateConverter(value.createdOn, true)}</p>
                                </Card>
                            )))
                    }
                    <Pagination
                        defaultCurrent={1}
                        defaultPageSize={numEachPage}
                        onChange={handleChange}
                        total={data.length}
                    />
                </div>
            </div>
        </div>
    )
}