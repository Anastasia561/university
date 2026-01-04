import React from 'react'
import '../styles/HomeStyles.css';

import {useContext} from "react";
import AuthContext from "../context/AuthProvider";
import {useTranslation} from "react-i18next";

function Home() {
    const {auth} = useContext(AuthContext);
    const { t } = useTranslation();

    return (
        <>
            <div className="img">
                <div className="img-text">
                    {auth?.role === "ROLE_ADMIN" && (
                        <>
                            <h1>{t("label.home.admin.wellcome")}</h1>
                            <p>{t("label.home.admin.descr")}</p>
                        </>
                    )}
                    {auth?.role === "ROLE_STUDENT" && (
                        <>
                            <h1>{t("label.home.student.wellcome")}</h1>
                            <p>{t("label.home.student.descr")}</p>
                        </>
                    )}
                    {!auth?.role && (
                        <>
                            <h1>{t("label.home.guest.wellcome")}</h1>
                            <p>{t("label.home.guest.descr")}</p>
                        </>
                    )}
                </div>
            </div>

            <div className="container">
                {auth?.role === "ROLE_ADMIN" && (
                    <>
                        <p>{t("label.home.admin.part1")}</p>
                        <p>{t("label.home.admin.part2")}<br/>
                            {t("label.home.admin.part3")}</p>
                    </>
                )}
                {auth?.role === "ROLE_STUDENT" && (
                    <>
                        <p>{t("label.home.admin.part1")}</p>
                        <p>{t("label.home.student.part2")}<br/>
                            {t("label.home.admin.part3")}</p>
                    </>
                )}
                {!auth?.role && (
                    <>
                        <p>{t("label.home.admin.part1")}</p>
                        <p>{t("label.home.guest.part2")}</p>
                    </>
                )}
            </div>
        </>
    );
}

export default Home;