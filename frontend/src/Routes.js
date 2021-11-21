import {Route, Switch} from "react-router-dom";
import Home from "./components/HomePage/Home";
import Login from "./components/Login/Login";
import NotFound from "./components/commonPages/NotFound";
import SignUp from "./components/SignUp/SignUp";

export default function Routes() {
    return (
        <Switch>
            <Route exact path="/">
                <Home />
            </Route>
            <Route exact path="/login">
                <Login />
            </Route>
            <Route exact path="/register">
                <SignUp />
            </Route>
            <Route>
                <NotFound />
            </Route>
        </Switch>
    )
}