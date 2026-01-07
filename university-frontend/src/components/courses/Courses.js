import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function Courses() {
    const [courses, setCourses] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();
    const { t } = useTranslation();

    useEffect(() => {
        const fetchCourses = async () => {
            try {
                let res;
                let path = `/api/courses?page=${currentPage}&size=${pageSize}`;
                if (auth?.accessToken) {
                    res = await authFetch(path, {}, auth, setAuth);
                } else {
                    res = await fetch(path);
                }

                const data = await res.json();

                setCourses(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setCourses([]);
            } finally {
                setLoading(false);
            }
        };

        fetchCourses();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>{t("label.course.loading")}</p>;
    }

    if (!Array.isArray(courses) || courses.length === 0) {
        return <h1>{t("label.course.no")}</h1>;
    }

    return (
        <>
            <h1>{t("label.course.data")}</h1>

            <div className="table-container">
                {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                    <div className="toolbar">
                        <Link className="link create-link" to="/courses/create">{t("label.course.create")}</Link>
                    </div>
                )}

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>{t("label.course.table.name")}</th>
                        <th>{t("label.course.table.code")}</th>
                        <th>{t("label.course.table.credit")}</th>
                        {auth.accessToken && (
                            <th colSpan="3"></th>
                        )}
                    </tr>
                    </thead>

                    <tbody>

                    {Array.isArray(courses) && courses.map((course, index) => (
                        <tr key={course.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
                            <td>{course.name}</td>
                            <td>{course.code}</td>
                            <td>{course.credit}</td>
                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <>
                                    <td>
                                        <Link className="link delete-link"
                                              to={`/courses/${course.id}/delete`}>{t("label.general.delete")}</Link>
                                    </td>
                                    <td>
                                        <Link className="link update-link"
                                              to={`/courses/${course.id}/edit`}>{t("label.general.update")}</Link>
                                    </td>
                                </>
                            )}

                            {auth.accessToken && (auth.role === "ROLE_STUDENT" || auth.role === "ROLE_ADMIN") && (
                                <td>
                                    <Link className="link view-link" to={`/courses/${course.id}`}>{t("label.general.details")}</Link>
                                </td>
                            )}
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="pagination">
                <button
                    disabled={currentPage === 0 || loading}
                    onClick={() => setCurrentPage(prev => prev - 1)}>
                    {t("label.general.page.prev")}
                </button>

                <span>{t("label.general.page.page")} {currentPage + 1} {t("label.general.page.of")} {totalPages}</span>

                <button
                    disabled={currentPage + 1 >= totalPages || loading}
                    onClick={() => setCurrentPage(prev => prev + 1)}>
                    {t("label.general.page.next")}
                </button>
            </div>
        </>
    );
}

export default Courses;