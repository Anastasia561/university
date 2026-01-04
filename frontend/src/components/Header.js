import {Link} from "react-router-dom";
import {useContext} from "react";
import AuthContext from "../context/AuthProvider";
import useLogout from "./auth/Logout";
import "../styles/HeaderStyles.css"
import {useTranslation} from "react-i18next";

const Header = () => {
    const {auth} = useContext(AuthContext);
    const { t } = useTranslation();
    const logout = useLogout();

    return (
        <nav className="navbar">
            <div className="nav-container">
                <Link to="/home" className="nav-logo">{t("label.nav.header")}</Link>

                <ul className="nav-links">
                    {!auth.accessToken && (
                        <>
                            <li><Link to="/courses">{t("label.nav.courses")}</Link></li>
                            <li><Link to="/login">{t("label.login.login")}</Link></li>
                        </>
                    )}

                    {auth.accessToken && auth.role === "ROLE_STUDENT" && (
                        <>
                            <li><Link to="/courses">{t("label.nav.student.courses")}</Link></li>
                            <li><Link to="/enrollments">{t("label.nav.student.enrollments")}</Link></li>
                            <li><Link to="#" onClick={logout}>{t("label.nav.logout")}</Link></li>
                        </>
                    )}

                    {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                        <>
                            <li><Link to="/students">{t("label.nav.students")}</Link></li>
                            <li><Link to="/courses">{t("label.nav.courses")}</Link></li>
                            <li><Link to="/enrollments">{t("label.nav.enrollments")}</Link></li>
                            <li><Link to="#" onClick={logout}>{t("label.nav.logout")}</Link></li>
                        </>
                    )}
                </ul>
            </div>
        </nav>
    );
};

export default Header;