import {useState} from "react";
import {Card, Pagination} from "antd";
import {dateConverter} from "../../helpers/DateConverter";
import {Link} from "react-router-dom";

export default function CommentsList(props) {
    const [data] = [props.data];
    const loading = props.loading;
    const numEachPage = props.numberEachPage;
    const [minValue, setMinValue] = useState(0)
    const [maxValue, setMaxValue] = useState(numEachPage)

    const handleChange = (value) => {
        setMinValue((value - 1) * numEachPage)
        setMaxValue(value * numEachPage)
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
                            <p className="text-start">{value.text}</p>
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