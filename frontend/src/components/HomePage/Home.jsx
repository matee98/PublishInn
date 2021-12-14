import {Component} from "react";

export default class Home extends Component{
    render(){
        if (localStorage.getItem('username')){
            return (
                <div className="Home">
                    <div className="lander">
                        <h1>Witaj, {localStorage.getItem('username')}</h1>
                    </div>
                </div>
            );
        }
        return (
            <div className="Home">
                <div className="lander">
                    <h1>Witaj</h1>
                </div>
            </div>
        );
    }
}