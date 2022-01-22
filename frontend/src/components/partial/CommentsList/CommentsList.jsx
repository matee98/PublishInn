import {useEffect, useState} from "react";
import {Card, Pagination} from "antd";
import {dateConverter} from "../../helpers/DateConverter";
import {Link} from "react-router-dom";
import {getCurrentUser} from "../../helpers/GetCurrentUser";
import {Button} from "react-bootstrap";
import axios from "axios";
import {useNotification} from "../Notifications/NotificationProvider";
import {useDialogPermanentChange} from "../CriticalOperations/CriticalOperationsProvider";
import {ResponseMessages} from "../../helpers/ResponseMessages";

export default function CommentsList(props) {
    const [data, setData] = useState({...props.data});
    const loading = props.loading;
    const numEachPage = props.numberEachPage;
    const [minValue, setMinValue] = useState(0)
    const [maxValue, setMaxValue] = useState(numEachPage)

    const [commentContent, setCommentContent] = useState("")
    const [editingId, setEditingId] = useState(0)

    const dispatch = useNotification();
    const dispatchDialog = useDialogPermanentChange();
    const blockedComment = "Ten komentarz został usunięty przez moderatora."

    useEffect(() => {
        setData(props.data)
    }, [props.data])

    const refreshData = () => {
        axios.get(`/comments/work/${data[0].workId}`)
            .then(res => {
                setData(res.data)
            })
    }

    const handleChange = (value) => {
        setMinValue((value - 1) * numEachPage)
        setMaxValue(value * numEachPage)
    }

    const handleEdit = (commentId, content) => {
        setEditingId(commentId)
        setCommentContent(content)
    }

    const handleSubmitEdit = (commentId) => {
        axios.put("/comments", {
            commentId: commentId,
            text: commentContent
        })
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.COMMENT_ADDED,
                    title: "Success"
                })
                setCommentContent("")
                setEditingId(0)
                refreshData()
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    const handleCancel = () => {
        setCommentContent("")
        setEditingId(0)
    }

    const handleDelete = (commentId) => {
        axios.delete(`/comments/delete/${commentId}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.COMMENT_DELETED,
                    title: "Success"
                })
                refreshData()
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    const handleChangeVis = (commentId) => {
        axios.patch(`/comments/changeVisibility/${commentId}`)
            .then(() => {
                dispatch({
                    type: "SUCCESS",
                    message: ResponseMessages.COMMENT_HIDESHOW,
                    title: "Success"
                })
                refreshData()
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: ResponseMessages.ERROR,
                    title: "Error"
                })
            })
    }

    return (
        <div>
            {
                data &&
                data.length > 0 &&
                data.slice(minValue, maxValue).map((value => (
                    <Card
                        type="inner"
                        title={
                            <Link to={`/users/profile/${value.username}`}>{value.username}</Link>
                        }
                        loading={loading}
                        extra={
                            <p>Dodano: {dateConverter(value.createdOn, true)}</p>
                        }
                        style={{
                            marginBottom: 16
                        }}
                    >
                        <div className="row">
                            {editingId === value.id ?
                                <>
                                    <textarea
                                        value={commentContent}
                                        className="col-10"
                                        onChange={
                                            (event) => {
                                                setCommentContent(event.target.value)
                                            }}
                                        style={{
                                            minWidth: "50%"
                                        }}
                                        rows={3}
                                    />
                                    <div className="col">
                                        <Button className="float-end mb-2" onClick={() => {
                                            handleSubmitEdit(value.id)
                                        }}>Zapisz</Button>
                                        <Button className="float-end" onClick={handleCancel}>Anuluj</Button>
                                    </div>
                                </> :
                                <>
                                    {value.visible ?
                                        <p className="text-start col-10">{value.text}</p>
                                        :
                                        <p className="text-start col-10" style={{
                                            fontStyle: "italic"
                                        }}>{blockedComment}</p>
                                    }
                                    <div className="col">
                                        <div className="row">
                                        {
                                            value.username === getCurrentUser().sub &&
                                                <>
                                                    <Button className="col mb-2 mx-1" onClick={() => {
                                                        handleEdit(value.id, value.text)
                                                    }}>Edytuj</Button>
                                                    <Button className="col mb-2 mx-1" onClick={() => {
                                                        dispatchDialog({
                                                            callbackOnSave:() => {
                                                                handleDelete(value.id)
                                                            }
                                                        })
                                                    }}>Usuń</Button>
                                                </>
                                        }
                                        {
                                            getCurrentUser().roles[0] === "MODERATOR" &&
                                            <Button className="col mx-1" onClick={() => {
                                                handleChangeVis(value.id)
                                            }}>
                                                {value.visible ? 'Zablokuj' : 'Odblokuj' }
                                            </Button>
                                        }
                                        </div>
                                    </div>

                                </>
                            }
                        </div>
                    </Card>
                )))
            }
            <Pagination
                defaultCurrent={1}
                defaultPageSize={numEachPage}
                onChange={handleChange}
                total={data.length}
                style={{
                    marginBottom: 16,
                    textAlign: "center"
                }}
            />
        </div>
    )
}