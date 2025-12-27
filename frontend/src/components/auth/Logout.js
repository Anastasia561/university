import {useContext} from "react";
import AuthContext from "../../context/AuthProvider";
import {useNavigate} from "react-router-dom";

const useLogout = () => {
    const {auth, setAuth} = useContext(AuthContext);
    const navigate = useNavigate();

    const logout = async () => {
        try {
            const response = await fetch("/api/auth/logout", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Authorization": `Bearer ${auth.accessToken}`
                }
            });

            if (!response.ok) {
                const data = await response.json();
                console.log(data.message);
                return;
            }

            setAuth({});
            navigate("/login");
        } catch (err) {
            console.error(err);
            setAuth({});
        }
    };

    return logout;
};

export default useLogout;