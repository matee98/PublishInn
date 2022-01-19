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
import SendResetPassword from "./components/ResetPassword/SendResetPassword";
import ResetPasswordConfirm from "./components/ResetPassword/ResetPasswordConfirm";
import BrowseBlocked from "./components/BrowseWorksList/BrowseBlocked";
import UserPanel from "./components/UserPanel/UserPanel";
import EmailConfirm from "./components/SignUp/EmailConfirm";
import {getCurrentUser} from "./components/helpers/GetCurrentUser";
import Forbidden from "./components/commonPages/Forbidden";
import SearchResults from "./components/Search/SearchResults";

export default class Routes extends Component{
    render() {
        return (
            <Switch>
                <Route exact path="/">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <Home />}
                </Route>
                <Route path="/user/panel">
                    {!localStorage.getItem('token') ? <Redirect to="/" /> : <UserPanel />}
                </Route>
                <Route exact path="/login">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <Login />}
                </Route>
                <Route exact path="/password/reset">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <SendResetPassword />}
                </Route>
                <Route exact path="/password/reset/confirm/:code">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <ResetPasswordConfirm />}
                </Route>
                <Route exact path="/register">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <SignUp />}
                </Route>
                <Route exact path="/register/confirm/:token">
                    {localStorage.getItem('token') ? <Redirect to="/user/panel" /> : <EmailConfirm />}
                </Route>
                <Route exact path="/account/info">
                    {localStorage.getItem('token') ? <AccountInfo /> : <Redirect to="/login" />}
                </Route>
                <Route exact path="/accounts">
                    {getCurrentUser().roles[0] === "ADMIN" ? <UsersAccountsList /> : <Redirect to='/common/forbidden' />}
                </Route>
                <Route path='/users/profile/:username'>
                    <UserProfile />
                </Route>
                <Route path='/users/info/:username'>
                    {getCurrentUser().roles[0] === "ADMIN" ? <OtherAccountInfo /> : <Redirect to='/common/forbidden' />}
                </Route>
                <Route path='/users/edit/:username'>
                    {getCurrentUser().roles[0] === "ADMIN" ? <EditAccount /> : <Redirect to='/common/forbidden' />}
                </Route>
                <Route exact path='/works/new'>
                    {localStorage.getItem('token') ? <AddWork /> : <Redirect to="/login" />}
                </Route>
                <Route exact path='/works/blocked'>
                    {getCurrentUser().roles[0] === "MODERATOR" ? <BrowseBlocked /> : <Redirect to='/common/forbidden' />}
                </Route>
                <Route path='/search/:query'>
                    <SearchResults />
                </Route>
                <Route exact path='/works/:type'>
                    <BrowseWorksList />
                </Route>
                <Route path='/works/read/:id'>
                    <WorkReadingView />
                </Route>
                <Route path='/common/forbidden'>
                    <Forbidden />
                </Route>
                <Route>
                    <NotFound />
                </Route>
            </Switch>
        )
    }
}