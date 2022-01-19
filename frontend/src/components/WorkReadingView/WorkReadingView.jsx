import {Link, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import ReactStars from "react-rating-stars-component";
import axios from "axios";
import CategoryMenu from "../partial/CategoryMenu";
import {dateConverter} from "../helpers/DateConverter";
import SideUserProfile from "../partial/SideUserProfile";
import {Button, Dropdown, DropdownButton, Form} from "react-bootstrap";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {getCurrentUser} from "../helpers/GetCurrentUser";
import MDEditor from "@uiw/react-md-editor";
import CommentsList from "../partial/CommentsList/CommentsList";
import BreadCrumb from "../partial/Breadcrumb";

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

    const [commentData, setCommentData] = useState([
        {
            username: "",
            text: "",
            visible: "",
            createdOn: ""
        }
    ])

    const [commentContent, setCommentContent] = useState("")

    const [rating, setRating] = useState(0)
    const [blocked, setBlocked] = useState(false)
    const [workBlocked, setWorkBlocked] = useState(false)
    const [comments, setComments] = useState(false)
    const [commentsLoading, setCommentsLoading] = useState(false);

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
                })
        } else {
            axios.get(`/works/details/${id}`)
                .then((res) => {
                    setData({
                        ...res.data,
                        author: res.data.userId
                    });
                    if (res.data.status === "BLOCKED") {
                        setWorkBlocked(true);
                    } else {
                        setWorkBlocked(false);
                    }
                })

            axios.get(`/ratings?username=${user.sub}&work_id=${id}`)
                .then((res) => {
                    setRating(res.data.rate)
                    setBlocked(true)
                })
        }
    }

    const fetchComments = () => {
        setCommentsLoading(true)
        instance.get(`/comments/work/${id}`)
            .then((res) => {
                setCommentData(res.data)
                setComments(true)
            })
            .then(() => {
                setCommentsLoading(false)
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: "Wystąpił błąd. Spróbuj ponownie później",
                    title: "Error"
                })
            })
    }

    const handleBlock = () => {
        axios.patch(`/works/moderator/block/${id}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Utwór został zablokowany",
                    title: "Success"
                })
                setWorkBlocked(true);
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
                setWorkBlocked(false);
            })
    }

    const handleDownloadPdf = () => {
        const FileSaver = require('file-saver');
        FileSaver.saveAs(`http://localhost:8080/api/works/convert/pdf/${id}`, `${data.title}_${data.username}.pdf`)
    }

    const handleCommentAdd = () => {
        axios.post("/comments", {
            workId: id,
            text: commentContent
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Komentarz został dodany",
                    title: "Success"
                })
                setCommentContent("")
                fetchComments()
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: "Wystąpił błąd. Spróbuj ponownie później",
                    title: "Error"
                })
            })
    }

    return (
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item"><Link to="/works/prose" className="breadcrumb-item-nonactive">Przeglądaj</Link></li>
                <li className="breadcrumb-item active">{data.title}</li>
            </BreadCrumb>
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
                            <MDEditor.Markdown
                                source={data.text}
                                style={{
                                    textAlign: "left",
                                    padding: "2px",
                                    borderBottom: "2px outset"
                                }}/>
                            {user.roles[0] === "MODERATOR" &&
                                    <p className="text-start mt-2">
                                        {workBlocked ?
                                            <Button onClick={handleUnblock}>Odblokuj utwór</Button>
                                            :
                                            <Button onClick={handleBlock}>Zablokuj utwór</Button>
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
                    <div className="text-start mx-3">
                        {comments ?
                            <div>
                                <p className="h5">Komentarze: </p>
                                {localStorage.getItem("token") &&
                                <div>
                                    <p className="h6 mt-4">
                                        Dodaj komentarz:
                                    </p>
                                    <div>
                                        <textarea
                                            value={commentContent}
                                            onChange={
                                                (event) => {
                                                    setCommentContent(event.target.value)
                                                }}
                                            style={{
                                                minWidth: "50%"
                                            }}
                                            rows={3}
                                        />
                                        <Button className="mx-3 mb-4" onClick={handleCommentAdd}>Zapisz</Button>
                                    </div>
                                </div>
                                }
                                <CommentsList
                                    data={commentData}
                                    loading={commentsLoading}
                                    numberEachPage={10}
                                />
                            </div> :
                            <p><a className="text-decoration-none" onClick={fetchComments}>Pokaż komentarze</a></p>
                        }
                    </div>
                </div>
                <div className="col-2 py-5">
                    <SideUserProfile username={data.username}/>
                </div>
            </div>
        </div>
    )
}