import {Link, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import axios from "axios";
import {dateConverter} from "../helpers/DateConverter";
import WorksList from "../partial/WorksList/WorksList";
import BreadCrumb from "../partial/Breadcrumb";

export default function UserProfile() {

    const { username } = useParams();
    const [loading, setLoading] = useState(true)
    const [data, setData] = useState({
        role: "",
        worksCount: "",
        joinDate: "",
        ratingAverage: ""
    })

    const [workData, setWorkData] = useState({
        id: "",
        title: "",
        username: "",
        type: "",
        rating: "",
        createdOn: ""
    })

    const dispatch = useNotification();
    const instance = axios.create();
    delete instance.defaults.headers.common["Authorization"];

    const fetchData = () => {
        if (username !== "") {
            instance.get(`/users/profile/short/${username}`)
                .then((res) => {
                    setData({
                        ...res.data,
                        joinDate: dateConverter(res.data.createdOn, false),
                        role: res.data.userRole
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

        instance.get(`/works/user/${username}`)
            .then((res) => {
                setWorkData(res.data)
            })
    }

    useEffect(() => {
        fetchData();
    }, [username])

    useEffect(() => {
        if (data.role === "USER"){
            setData({
                ...data,
                role: "Użytkownik"
            })
        }
        if (data.role === "MODERATOR"){
            setData({
                ...data,
                role: "Moderator"
            })
        }
        if (data.role === "ADMIN"){
            setData({
                ...data,
                role: "Administrator"
            })
        }
    }, [data])

    useEffect(() => {
        if (workData[0] !== undefined) {
            if (workData[0].title !== "") {
                setLoading(false)
            }
        }
    }, [workData])
    return(
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item active">Profil użytkownika {username}</li>
            </BreadCrumb>
            <div className="row">
                <div className="col-md-3 border-right">
                    <div className="d-flex flex-column align-items-center text-center p-3 py-5"><img className="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg" /><span className="font-weight-bold">{username}</span><span> </span></div>
                </div>
                <div className="col-md-5 border-right">
                    <div className="p-3 py-5">
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h4 className="text-right">Profil użytkownika</h4>
                        </div>
                        <div className="row mt-3">
                            <div className="flex-md-row-reverse"><label className="labels float-start">Data dołączenia</label><label className="labels float-end">{data.joinDate}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Rola użytkownika</label><label className="labels float-end">{data.role}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Liczba utworów</label><label className="labels float-end">{data.worksCount}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Średnia ocen</label><label className="labels float-end">{data.ratingAverage}</label></div>
                            <div className="mt-2 mb-2 text-start fs-5">Utwory dodane przez tego użytkownika:</div>
                            <WorksList
                                data={workData}
                                loading={loading}
                                numberEachPage={3}
                                />
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}