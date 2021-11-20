import './App.css';
import NavigationBar from "./components/partial/Navbar";
import style from 'bootstrap/dist/css/bootstrap.css';
import Routes from "./Routes"

function App() {
  return (
    <div className="App">
        <NavigationBar/>
        <Routes/>
    </div>
  );
}

export default App;
