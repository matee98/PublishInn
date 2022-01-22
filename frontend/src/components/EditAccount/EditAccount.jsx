import {Link, useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import axios from "axios";
import {AppRoles} from "../helpers/AppRoles";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import BreadCrumb from "../partial/Breadcrumb";
import {ResponseMessages} from "../helpers/ResponseMessages";

export default function EditAccount() {
    const { username } = useParams();
    const [data, setData] = useState({
        mailAddress: "",
        userRole: ""
    });
    const dispatch = useNotification();

    useEffect(() => {
        axios.get(`/users/admin/${username}`)
            .then((res) => {
                setData({
                    mailAddress: res.data.email,
                    userRole: res.data.appUserRole,
                });
            })
    }, [])

    const saveChanges = () => {
        axios.put(`/users/admin/edit/${username}`, data)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.CHANGES_SAVED,
                    title: "Success"
                })
            })
            .catch(err => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }


    return(
        <div className="container-fluid">
            <BreadCrumb>
                <li className="breadcrumb-item"><Link to="/" className="breadcrumb-item-nonactive">Start</Link></li>
                <li className="breadcrumb-item"><Link to="/accounts" className="breadcrumb-item-nonactive">Lista użytkowników</Link></li>
                <li className="breadcrumb-item"><Link to={`/users/info/${username}`} className="breadcrumb-item-nonactive">{username}</Link></li>
                <li className="breadcrumb-item active">Edytuj</li>
            </BreadCrumb>
            <div className="row">
                <div className="col-md-3 border-right">
                    <div className="d-flex flex-column align-items-center text-center p-3 py-5"><img className="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg" /><span class="font-weight-bold">{data.username}</span><span> </span></div>
                </div>
                <div className="col-md-5 border-right">
                    <div className="p-3 py-5">
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h4 className="text-right">Edycja konta</h4>
                        </div>
                        <div className="row mt-3">
                            <div className="flex-md-row-reverse">
                                <label className="labels float-start">Adres e-mail</label>
                                <input type="email" id="userMail" value={data.mailAddress}
                                       onChange={(event => setData({...data, mailAddress: event.target.value}))}
                                       className="form-control" />
                            </div>
                            <div className="flex-md-row-reverse">
                                <label className="labels float-start">Rola użytkownika</label>
                                <select className="form-select" value={data.userRole}
                                onChange={(event => setData({...data, userRole: event.target.value}))}>
                                    <option value="ADMIN">{AppRoles[0]}</option>
                                    <option value="MODERATOR">{AppRoles[1]}</option>
                                    <option value="USER">{AppRoles[2]}</option>
                                </select>
                                <button className="btn btn-primary profile-button mt-2" type="button" onClick={saveChanges}>Zapisz</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}