import {Redirect, Route, Switch} from "react-router-dom";
import {Component} from "react";
import Home from "./components/HomePage/Home";
import Login from "./components/Login/Login";
import NotFound from "./components/commonPages/NotFound";
import SignUp from "./components/SignUp/SignUp";
import AccountInfo from "./components/AccountInfo/AccountInfo";
import UsersAccountsList from "./components/UsersAccountsList/UsersAccountsList";
import OtherAccountInfo from "./components/OtherAccountInfo/OtherAccountInfo";
import EditAccount from "./components/EditAccount/EditAccount";
import AddWork from "./components/AddWork/AddWork";
import WorkReadingView from "./components/WorkReadingView/WorkReadingView";
import BrowseWorksList from "./components/BrowseWorksList/BrowseWorksList";
import UserProfile from "./components/UserProfile/UserProfile";
import NewWork from "./components/AddWork/NewWork";
import SendResetPassword from "./components/ResetPassword/SendResetPassword";
import ResetPasswordConfirm from "./components/ResetPassword/ResetPasswordConfirm";

export default class Routes extends Component{
    render() {
        return (
            <Switch>
                <Route exact path="/">
                    <Home />
                </Route>
                <Route exact path="/login">
                    {localStorage.getItem('token') ? <Redirect to="/" /> : <Login />}
                </Route>
                <Route exact path="/password/reset">
                    {localStorage.getItem('token') ? <Redirect to="/" /> : <SendResetPassword />}
                </Route>
                <Route exact path="/password/reset/confirm/:code">
                    {localStorage.getItem('token') ? <Redirect to="/" /> : <ResetPasswordConfirm />}
                </Route>
                <Route exact path="/register">
                    {localStorage.getItem('token') ? <Redirect to="/" /> : <SignUp />}
                </Route>
                <Route exact path="/account/info">
                    {localStorage.getItem('token') ? <AccountInfo /> : <Redirect to="/login" />}
                </Route>
                <Route exact path="/accounts">
                    <UsersAccountsList />
                </Route>
                <Route path='/users/profile/:username'>
                    <UserProfile />
                </Route>
                <Route path='/users/info/:username'>
                    <OtherAccountInfo />
                </Route>
                <Route path='/users/edit/:username'>
                    <EditAccount />
                </Route>
                <Route exact path='/works/new'>
                    {localStorage.getItem('token') ? <AddWork /> : <Redirect to="/login" />}
                </Route>
                <Route exact path='/test/new'>
                    <NewWork />
                </Route>
                <Route exact path='/works/:type'>
                    <BrowseWorksList />
                </Route>
                <Route path='/works/read/:id'>
                    <WorkReadingView />
                </Route>
                <Route>
                    <NotFound />
                </Route>
            </Switch>
        )
    }
}