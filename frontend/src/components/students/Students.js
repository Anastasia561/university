import React, {useContext, useEffect, useState} from 'react';
import '../../styles/TableStyles.css';
import {Link, useNavigate} from 'react-router-dom';
import AuthContext from "../../context/AuthProvider";
import {authFetch} from "../auth/AuthFetch";
import {useTranslation} from "react-i18next";

function Students() {
    const [students, setStudents] = useState([]);
    const [loading, setLoading] = useState(true);

    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize] = useState(5);

    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();
    const { t } = useTranslation();

    useEffect(() => {
        const fetchEnrollments = async () => {
            try {
                let path = `/api/students?page=${currentPage}&size=${pageSize}`;
                const res = await authFetch(path, {}, auth, setAuth);
                const data = await res.json();

                setStudents(Array.isArray(data.content) ? data.content : []);
                setCurrentPage(data.number);
                setTotalPages(data.totalPages);
            } catch (err) {
                if (err.message === 'Session expired') {
                    navigate('/login');
                }
                console.error(err);
                setStudents([]);
            } finally {
                setLoading(false);
            }
        };

        fetchEnrollments();
    }, [auth?.accessToken, currentPage]);

    if (loading) {
        return <p>{t("label.student.loading")}</p>;
    }

    return (
        <>
            <h1>{t("label.student.data")}</h1>

            <div className="table-container">
                <div className="toolbar">
                    <Link className="link create-link" to="/students/create">{t("label.student.create")}</Link>
                </div>

                <table>
                    <thead>
                    <tr>
                        <th>#</th>
                        <th>{t("label.student.fname")}</th>
                        <th>{t("label.student.lname")}</th>
                        <th>{t("label.student.email")}</th>
                        <th colSpan="3"></th>
                    </tr>
                    </thead>

                    <tbody>
                    {Array.isArray(students) && students.map((student, index) => (
                        <tr key={student.id}>
                            <td>{index + 1 + currentPage * pageSize}</td>
                            <td>{student.firstName}</td>
                            <td>{student.lastName}</td>
                            <td>{student.email}</td>
                            <td>
                                <Link className="link delete-link" to={`/students/${student.id}/delete`}>
                                    {t("label.general.delete")}</Link>
                            </td>
                            <td>
                                <Link className="link update-link" to={`/students/${student.id}/edit`}>
                                    {t("label.general.update")}</Link>
                            </td>
                            <td>
                                <Link className="link view-link" to={`/students/${student.id}`}>
                                    {t("label.general.details")}</Link>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>

            <div className="pagination">
                <button
                    disabled={currentPage === 0 || loading}
                    onClick={() => setCurrentPage(prev => prev - 1)}>{t("label.general.page.prev")}
                </button>

                <span>{t("label.general.page.page")} {currentPage + 1} {t("label.general.page.of")} {totalPages}</span>

                <button
                    disabled={currentPage + 1 >= totalPages || loading}
                    onClick={() => setCurrentPage(prev => prev + 1)}>{t("label.general.page.next")}
                </button>
            </div>
        </>
    );
}

export default Students;
