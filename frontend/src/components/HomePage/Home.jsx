import {Component} from "react";
import {Button} from "react-bootstrap";
import {Link} from "react-router-dom";

export default class Home extends Component{
    render(){
        if (localStorage.getItem('username')){
            return (
                <div className="Home">
                    <div className="lander">
                        <h1>Witaj, {localStorage.getItem('username')}</h1>
                        <Button>
                            <Link to='/test/new'>
                                Dodaj pracę
                            </Link>
                        </Button>
                    </div>
                </div>
            );
        }
        return (
            <div className="Home">
                <div className="lander">
                    <h1>Witaj</h1>
                    <Button>
                        <Link to='/test/new'>
                            Dodaj pracę
                        </Link>
                    </Button>
                </div>
            </div>
        );
    }
}