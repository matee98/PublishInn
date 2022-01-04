import {Link} from "react-router-dom";
import {useState} from "react";
import './CategoryMenu.css';

export default function CategoryMenu() {

    const [dropdown, setDropdown] = useState(false);

    const handleClickProseDropdown = () => {
        setDropdown(!dropdown);
    }

    return (
        <div className="text-start px-3">
            <p className="fs-5">
                <Link to="" className="text-decoration-none text-dark">
                    Proza
                </Link>
                <span className="clickableIcon">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         className="bi bi-arrow-down" viewBox="0 0 16 16"
                         onClick={handleClickProseDropdown}>
                        <path fill-rule="evenodd"
                              d="M8 1a.5.5 0 0 1 .5.5v11.793l3.146-3.147a.5.5 0 0 1 .708.708l-4 4a.5.5 0 0 1-.708 0l-4-4a.5.5 0 0 1 .708-.708L7.5 13.293V1.5A.5.5 0 0 1 8 1z"/>
                    </svg>
                </span>
            </p>
            {
                dropdown &&
                <ul className="list-group list-group-flush">
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Fantasy
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Horror
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Inne
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Kryminał
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Obyczajowe
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Science-fiction
                        </Link>
                    </li>
                    <li className="list-group-item">
                        <Link to="" className="text-decoration-none text-dark">
                            Thriller
                        </Link>
                    </li>
                </ul>
            }
            <p className="fs-5">
                <Link to="" className="text-decoration-none text-dark">
                    Poezja
                </Link>
            </p>
        </div>
    )
}