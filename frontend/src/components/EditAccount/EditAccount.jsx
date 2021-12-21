import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {AppRoles} from "../helpers/AppRoles";

export default function EditAccount() {
    const { username } = useParams();
    const [data, setData] = useState({
        username: "",
        mailAddress: "",
        userRole: ""
    });

    useEffect(() => {
        axios.get(`/users/${username}`)
            .then((res) => {
                setData({
                    ...res.data,
                    mailAddress: res.data.email,
                    userRole: res.data.appUserRole,
                });
            })
    }, [])


    return(
        <div className="container-fluid">
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
                                <input type="email" id="userMail" value={data.mailAddress} className="form-control" />
                            </div>
                            <div className="flex-md-row-reverse">
                                <label className="labels float-start">Rola użytkownika</label>
                                <select className="form-select" value={data.userRole}>
                                    <option value="ADMIN">{AppRoles[0]}</option>
                                    <option value="MODERATOR">{AppRoles[1]}</option>
                                    <option value="USER">{AppRoles[2]}</option>
                                </select>
                                <button className="btn btn-primary profile-button mt-2" type="button">Zapisz</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}