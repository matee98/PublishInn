import {Card, Pagination} from "antd";
import {Link} from "react-router-dom";
import {Button} from "react-bootstrap";
import {dateConverter} from "../../helpers/DateConverter";
import {useEffect, useState} from "react";

export default function WorksList(props) {

    const [data, setData] = [props.data];
    const loading = props.loading;
    const numEachPage = props.numberEachPage;
    const [minValue, setMinValue] = useState(0)
    const [maxValue, setMaxValue] = useState(numEachPage)

    const handleChange = (value) => {
        setMinValue((value - 1) * numEachPage)
        setMaxValue(value * numEachPage)
    }

    useEffect(() => {
        console.log(data)
    }, [data])

    return (
        <div>
            {
                data &&
                data.length > 0 &&
                data.slice(minValue, maxValue).map((value => (
                    <Card
                        title={value.title}
                        extra={
                            <Link to={`/works/read/${value.id}`}>
                                <Button>
                                    Czytaj >>>
                                </Button>
                            </Link>
                        }
                        style={{
                            width: "auto"
                        }}
                        loading={loading}
                    >
                        <p className="text-start">
                            Autor: <span><Link to={`/users/profile/${value.username}`}>{value.username}</Link></span>
                        </p>
                        <p className="text-start">Ocena: {value.rating !== null ? value.rating : "brak"}</p>
                        <p className="text-start">Dodano: {dateConverter(value.createdOn, true)}</p>
                    </Card>
                )))
            }
            <Pagination
                defaultCurrent={1}
                defaultPageSize={numEachPage}
                onChange={handleChange}
                total={data.length}
            />
        </div>
    )
}