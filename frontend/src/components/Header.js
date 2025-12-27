import {Link} from "react-router-dom";
import {useContext} from "react";
import AuthContext from "../context/AuthProvider";
import useLogout from "./auth/Logout";
import "../styles/HeaderStyles.css"

const Header = () => {
    const {auth} = useContext(AuthContext);

    const logout = useLogout();

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/home" className="nav-logo">University</Link>

                <ul className="nav-links">
                    {!auth.accessToken && (
                        <>
                            <li><Link to="/courses">Courses</Link></li>
                            <li><Link to="/login">Login</Link></li>
                        </>
                    )}

                    {auth.accessToken && auth.role === "ROLE_STUDENT" && (
                        <>
                            <li><Link to="/courses">My Courses</Link></li>
                            <li><Link to="/enrollments">My Enrollments</Link></li>
                            <li><Link to="#" onClick={logout}>Logout</Link></li>
                        </>
                    )}

                    {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                        <>
                            <li><Link to="/students">Students</Link></li>
                            <li><Link to="/courses">Courses</Link></li>
                            <li><Link to="/enrollments">Enrollments</Link></li>
                            <li><Link to="#" onClick={logout}>Logout</Link></li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default Header;