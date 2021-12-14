import './App.css';
import NavigationBar from "./components/partial/Navbar";
import Routes from "./Routes"
import {Component} from "react";
import Footer from "./components/partial/Footer";
import ContentBox from "./components/partial/ContentBox";

export default class App extends Component {
    state = {};
    render() {
        return (
            <div className="App">
                <NavigationBar/>
                <ContentBox>
                    <Routes/>
                </ContentBox>
                <Footer />
            </div>
        );
    }
}
