import {useEffect, useState} from "react";
import {Link, useParams} from "react-router-dom";
import axios from "axios";
import {useNotification} from "../partial/Notifications/NotificationProvider";
import {useDialogPermanentChange} from "../partial/CriticalOperations/CriticalOperationsProvider";

export default function OtherAccountInfo() {
    const { username } = useParams();
    const [data, setData] = useState({
        username: "",
        mailAddress: "",
        userRole: "",
        enabled: false,
        locked: false
    });
    const dispatch = useNotification();
    const dispatchDialog = useDialogPermanentChange();

    useEffect(() => {
        fetchData()
    }, [])

    const fetchData = () => {
        axios.get(`/users/${username}`)
            .then((res) => {
                setData({
                    ...res.data,
                    mailAddress: res.data.email,
                    userRole: res.data.appUserRole,
                });
            })
    }

    const lockUser = () => {
        axios.patch(`/users/block/${username}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Konto zostało zablokowane",
                    title: "Success"
                })
            })
    }

    const unlockUser = () => {
        axios.patch(`/users/unblock/${username}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: "Konto zostało odblokowane",
                    title: "Success"
                })
            })
    }

    return(
        <div className="container-fluid">
            <div className="row">
                <div className="col-md-3 border-right">
                    <div className="d-flex flex-column align-items-center text-center p-3 py-5"><img className="rounded-circle mt-5" width="150px" src="https://st3.depositphotos.com/15648834/17930/v/600/depositphotos_179308454-stock-illustration-unknown-person-silhouette-glasses-profile.jpg" /><span class="font-weight-bold">{data.username}</span><span> </span></div>
                </div>
                <div className="col-md-5 border-right">
                    <div className="p-3 py-5">
                        <div className="d-flex justify-content-between align-items-center mb-3">
                            <h4 className="text-right">Informacje o koncie</h4>
                        </div>
                        <div className="row mt-3">
                            <div className="flex-md-row-reverse"><label className="labels float-start">Adres e-mail</label><label className="labels float-end">{data.mailAddress}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Rola użytkownika</label><label className="labels float-end">{data.userRole}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Konto potwierdzone</label><label className="labels float-end">{data.enabled ? "TAK" : "NIE"}</label></div>
                            <div className="flex-md-row-reverse"><label className="labels float-start">Konto zablokowane</label><label className="labels float-end">{data.locked ? "TAK" : "NIE"}</label></div>
                        </div>
                    </div>
                </div>
                <div className="col-md-2 border-right">
                    <div className="mt-5 float-start">
                        <Link to={`/users/edit/${username}`}>
                            <button className="btn btn-primary profile-button" type="button">Edytuj profil</button>
                        </Link>
                    </div>
                    <div className="mt-2 float-start">
                        {data.locked ?
                            <button className="btn btn-primary profile-button" type="button" onClick={unlockUser}>Odblokuj użytkownika</button>
                        :
                            <button className="btn btn-primary profile-button" type="button" onClick={() => {
                                dispatchDialog({
                                    callbackOnSave:() => {
                                        lockUser()
                                    }
                                })
                            }}>Zablokuj użytkownika</button>}
                    </div>
                </div>
            </div>
        </div>
    )
}