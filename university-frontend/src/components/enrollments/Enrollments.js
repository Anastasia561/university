import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function Enrollments() {
    const navigate = useNavigate();
    const [enrollments, setEnrollments] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);
    const { t } = useTranslation();

    useEffect(() => {
        const fetchEnrollments = async () => {
            try {
                let path = `/api/enrollments?page=${currentPage}&size=${pageSize}`;

                const res = await authFetch(path, {}, auth, setAuth);
                const data = await res.json();

                setEnrollments(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setEnrollments([]);
            } finally {
                setLoading(false);
            }
        };

        fetchEnrollments();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>{t("label.enrollment.loading")}</p>;
    }

    if (!Array.isArray(enrollments) || enrollments.length === 0) {
        return <h1>{t("label.enrollment.no")}</h1>;
    }

    return (
        <>
            <h1>{t("label.enrollment.data")}</h1>

            <div className="table-container">
                {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                    <div className="toolbar">
                        <Link className="link create-link" to="/enrollments/create">
                            {t("label.enrollment.create")}</Link>
                    </div>
                )}

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>{t("label.course.table.code")}</th>
                        <th>{t("label.student.email.student")}</th>
                        <th>{t("label.enrollment.date")}</th>
                        <th>{t("label.enrollment.grade")}</th>
                        {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                            <th colSpan="4"></th>
                        )}

                    </tr>
                    </thead>

                    <tbody>
                    {Array.isArray(enrollments) && enrollments.map((enrollment, index) => (
                        <tr key={enrollment.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
                            <td>{enrollment.courseCode}</td>
                            <td>{enrollment.studentEmail}</td>
                            <td>{enrollment.enrollmentDate}</td>
                            <td>{enrollment.finalGrade != null ? enrollment.finalGrade : '-'}</td>

                            {auth.accessToken && auth.role === "ROLE_ADMIN" && (
                                <>
                                    <td>
                                        <Link className="link delete-link"
                                              to={`/enrollments/${enrollment.id}/delete`}>
                                            {t("label.general.delete")}</Link>
                                    </td>
                                    <td>
                                        <Link className="link update-link"
                                              to={`/enrollments/${enrollment.id}/edit`}>
                                            {t("label.general.update")}</Link>
                                    </td>
                                    <td>
                                        <Link className="link view-link" to={`/enrollments/${enrollment.id}`}>
                                            {t("label.general.details")}</Link>
                                    </td>
                                </>

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

export default Enrollments;