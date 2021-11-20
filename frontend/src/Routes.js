import {Route, Switch} from "react-router-dom";
import Home from "./components/HomePage/Home";
import Login from "./components/Login/Login";

export default function Routes() {
    return (
        <Switch>
            <Route exact path="/">
                <Home />
            </Route>
            <Route exact path="/login">
                <Login />
            </Route>
        </Switch>
    )
}