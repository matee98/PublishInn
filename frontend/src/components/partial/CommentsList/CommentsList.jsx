import {useState} from "react";
import {Card, Pagination} from "antd";
import {dateConverter} from "../../helpers/DateConverter";
import {Link} from "react-router-dom";
import {getCurrentUser} from "../../helpers/GetCurrentUser";
import {Button} from "react-bootstrap";
import axios from "axios";
import {useNotification} from "../Notifications/NotificationProvider";
import {useDialogPermanentChange} from "../CriticalOperations/CriticalOperationsProvider";

export default function CommentsList(props) {
    const [data, setData] = useState(props.data);
    const loading = props.loading;
    const numEachPage = props.numberEachPage;
    const [minValue, setMinValue] = useState(0)
    const [maxValue, setMaxValue] = useState(numEachPage)

    const [commentContent, setCommentContent] = useState("")
    const [editingId, setEditingId] = useState(0)

    const dispatch = useNotification();
    const dispatchDialog = useDialogPermanentChange();

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
                    message: "Komentarz został edytowany",
                    title: "Success"
                })
                setCommentContent("")
                setEditingId(0)
                refreshData()
            })
            .catch(() => {
                dispatch({
                    type: "ERROR",
                    message: "Wystąpił błąd. Spróbuj ponownie później",
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
                    message: "Komentarz został usunięty",
                    title: "Success"
                })
                refreshData()
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
                                    <p className="text-start col-10">{value.text}</p>
                                    {
                                        value.username === getCurrentUser().sub &&
                                            <div className="col">
                                                <Button className="float-end mb-2" onClick={() => {
                                                    handleEdit(value.id, value.text)
                                                }}>Edytuj</Button>
                                                <Button className="float-end" onClick={() => {
                                                    dispatchDialog({
                                                        callbackOnSave:() => {
                                                            handleDelete(value.id)
                                                        }
                                                    })
                                                }}>Usuń</Button>
                                            </div>
                                    }
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