import {Link, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import ReactStars from "react-rating-stars-component";
import axios from "axios";
import MDEditor from "@uiw/react-md-editor";
import CategoryMenu from "../partial/CategoryMenu";
import {dateConverter} from "../helpers/DateConverter";
import SideUserProfile from "../partial/SideUserProfile";
import {Button} from "react-bootstrap";
import {useNotification} from "../partial/Notifications/NotificationProvider";

export default function WorkReadingView() {
    const { id } = useParams();
    const [data, setData] = useState({
        title: "",
        username: "",
        rating: "",
        text: "",
        createdOn: ""
    });

    const [rating, setRating] = useState(0)
    const [blocked, setBlocked] = useState(false)

    const dispatch = useNotification()

    const ratingChange = (newRating) => {
        if(rating === 0) {
            axios.post("/ratings", {
                workId: id,
                rate: newRating
            })
                .then(() => {
                    dispatch({
                        type: "SUCCESS",
                        message: "Zmiany zostały zapisane",
                        title: "Success"
                    })
                })
                .catch(err => {
                    dispatch({
                        type: "ERROR",
                        message: err.message,
                        title: "Error"
                    })
                })
        } else {
            axios.put("/ratings", {
                workId: id,
                rate: newRating
            })
                .then(() => {
                    dispatch({
                        type: "SUCCESS",
                        message: "Zmiany zostały zapisane",
                        title: "Success"
                    })
                })
                .catch(err => {
                    dispatch({
                        type: "ERROR",
                        message: err.message,
                        title: "Error"
                    })
                })
        }
        setRating(newRating)
        setBlocked(true)
    }
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

        axios.get(`/ratings?username=${localStorage.getItem("username")}&work_id=${id}`)
            .then((res) => {
                setRating(res.data.rate)
                setBlocked(true)
            })
    }

    return (
        <div className="container-fluid">
            <div className="row">
                <div className="col-2 py-5">
                    <CategoryMenu />
                </div>
                <div className="col-md border-start border-end">
                    <div className="p-3 py-3">
                        <div className="mb-3 align-content-md-start">
                            <p className="text-end">{dateConverter(data.createdOn, true)}</p>
                            <p className="text-end">Ocena: {data.rating}/10</p>
                            <h2 className="text-center">{data.title}</h2>
                            <MDEditor.Markdown
                                source={data.text}
                                style={{
                                    textAlign: "left",
                                    padding: "2px",
                                    borderBottom: "2px outset"
                                }}/>
                            {localStorage.getItem("username") ?
                                (blocked
                                    ?
                                    <div className="float-start d-inline-flex">
                                        <p style={{marginRight: "0.5rem"}}>Twoja ocena: {rating}</p>
                                        <Button className="btn-sm" onClick={() => {
                                            setBlocked(false)
                                        }}>Zmień</Button>
                                    </div>
                                    :
                                <div>
                                    <p className="text-start mt-4 fs-5">Oceń utwór:</p>
                                    <ReactStars
                                        count={10}
                                        size={36}
                                        value={rating}
                                        onChange={ratingChange}
                                        isHalf={false}
                                        emptyIcon={<i className="far fa-star"></i>}
                                        fullIcon={<i className="fa fa-star"></i>}
                                        activeColor="#ffd700"
                                    />
                                </div>
                                )
                                :
                                <div className="text-start mt-4 fs-5">
                                    <Link to="/login">
                                        Zaloguj się
                                    </Link>
                                    , aby ocenić utwór
                                </div>
                            }
                        </div>
                    </div>
                </div>
                <div className="col-2 py-5">
                    <SideUserProfile username={data.username}/>
                </div>
            </div>
        </div>
    )
}