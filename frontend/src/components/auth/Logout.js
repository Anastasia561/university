import {useContext} from "react";
import AuthContext from "../../context/AuthProvider";
import {useNavigate} from "react-router-dom";
import {authFetch} from "./AuthFetch";

const useLogout = () => {
    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();

    const logout = async () => {
        try {
            await authFetch(
                "/api/auth/logout",
                {
                    method: "POST"
                }, auth, setAuth
            );
        } catch (err) {
            console.error(err);
        } finally {
            setAuth({
                accessToken: null,
                email: null,
                role: null
            });
            navigate("/login");
        }
    };

    return logout;
};

export default useLogout;