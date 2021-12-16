import './App.css';
import NavigationBar from "./components/partial/Navbar";
import Routes from "./Routes"
import Footer from "./components/partial/Footer";
import ContentBox from "./components/partial/ContentBox";

export default function App() {
    return (
        <div className="App">
            <div className="App-header" />
            <NavigationBar />
            <ContentBox>
                <Routes />
            </ContentBox>
            <Footer />
        </div>
    );
}
