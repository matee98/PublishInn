import {Route, Switch} from "react-router-dom";
import Home from "./components/HomePage/Home";

export default function Routes() {
    return (
        <Switch>
            <Route exact path="/">
                <Home />
            </Route>
        </Switch>
    )
}