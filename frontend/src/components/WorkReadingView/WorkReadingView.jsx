import {Link, useParams} from "react-router-dom";
import React, {useCallback, useEffect, useMemo, useState} from "react";
import ReactStars from "react-rating-stars-component";
import axios from "axios";
import CategoryMenu from "../partial/CategoryMenu";
import {dateConverter} from "../helpers/DateConverter";
import SideUserProfile from "../partial/SideUserProfile";
import {Button, Dropdown, DropdownButton} from "react-bootstrap";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {getCurrentUser} from "../helpers/GetCurrentUser";
import {withReact, Slate, Editable} from "slate-react";
import {createEditor} from "slate";
import './../AddWork/NewWork.css'

export default function WorkReadingView() {
    const { id } = useParams();
    const [data, setData] = useState({
        title: "",
        username: "",
        rating: "",
        text: "",
        createdOn: "",
        status: ""
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
                    setRating(newRating)
                    setBlocked(true)
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
                    setRating(newRating)
                    setBlocked(true)
                })
                .catch(err => {
                    dispatch({
                        type: "ERROR",
                        message: err.message,
                        title: "Error"
                    })
                })
        }
    }

    const user = getCurrentUser();

    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const deserialize = string => {
        return string.split('\n').map(line => {
            return {
                children: [{ text: line}],
            }
        })
    }

    const editor = useMemo(() => withReact(createEditor()), [])
    const renderElement = useCallback(({ attributes, children, element }) => {
        return <p
            className="text-area"
            {...attributes}
        >
            {children}
        </p>
    }, [])

    const [textReader, setTextReader] = useState();

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        if (user.roles[0] === "GUEST") {
            instance.get(`/works/details/${id}`)
                .then((res) => {
                    setData({
                        ...res.data,
                        author: res.data.userId
                    });
                    setTextReader(
                        <Slate editor={editor} value={deserialize(res.data.text)} onChange={() => {}}>
                            <Editable readOnly renderElement={renderElement}/>
                        </Slate>
                    )
                })
        } else {
            axios.get(`/works/details/${id}`)
                .then((res) => {
                    setData({
                        ...res.data,
                        author: res.data.userId
                    });
                    setTextReader(
                        <Slate editor={editor} value={deserialize(res.data.text)} onChange={() => {}}>
                            <Editable readOnly renderElement={renderElement}/>
                        </Slate>
                    )
                })

            axios.get(`/ratings?username=${user.sub}&work_id=${id}`)
                .then((res) => {
                    setRating(res.data.rate)
                    setBlocked(true)
                })
        }
    }

    const handleBlock = () => {
        axios.patch(`/works/moderator/block/${id}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Utwór został zablokowany",
                    title: "Success"
                })
            })
    }

    const handleUnblock = () => {
        axios.patch(`/works/moderator/unblock/${id}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Utwór został odblokowany",
                    title: "Success"
                })
            })
    }

    const handleDownloadPdf = () => {
        const FileSaver = require('file-saver');
        FileSaver.saveAs(`http://localhost:8080/api/works/convert/pdf/${id}`, `${data.title}_${data.username}.pdf`)
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
                            <p className="float-start">
                                <DropdownButton title="Pobierz">
                                    <Dropdown.Item onClick={handleDownloadPdf}>PDF</Dropdown.Item>
                                </DropdownButton>
                            </p>
                            <p className="text-end">{dateConverter(data.createdOn, true)}</p>
                            <p className="text-end">Ocena: {data.rating}/10</p>
                            <h2 className="text-center">{data.title}</h2>
                            {textReader}
                            {user.roles[0] === "MODERATOR" &&
                                    <p className="text-start mt-2">
                                        {data.status === "ACCEPTED" ?
                                            <Button onClick={handleBlock}>Zablokuj utwór</Button>
                                            :
                                            <Button onClick={handleUnblock}>Odblokuj utwór</Button>
                                        }
                                    </p>
                            }
                            {localStorage.getItem("token") ?
                                (blocked
                                    ?
                                    <div className="float-start d-inline-flex">
                                        <p className="fs-6" style={{marginRight: "0.5rem"}}>Twoja ocena:
                                            <span className="fs-4 mx-1">{rating}</span>
                                        </p>
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