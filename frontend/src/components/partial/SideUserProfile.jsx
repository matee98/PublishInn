import {useEffect, useState} from "react";
import axios from "axios";
import {dateConverter} from "../helpers/DateConverter";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";
import {useNotification} from "./Notifications/NotificationProvider";

export default function SideUserProfile(props) {
    const username = props.username;
    const [data, setData] = useState({
        role: "",
        worksCount: "",
        joinDate: ""
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

    return (
        <div
        style={{
            border: "2px outset",
            padding: "6px"
        }}>
            <h4>AUTOR</h4>
            <p className="fs-5">{username}</p>
            <p>{data.role}</p>
            <p>Dołączył: {data.joinDate}</p>
            <p>Liczba utworów: {data.worksCount}</p>
            <Link to={`/users/profile/${username}`}>
                <Button className="btn-sm">Zobacz profil</Button>
            </Link>
        </div>
    )
}