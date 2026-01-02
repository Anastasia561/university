import React, {useState, useContext} from "react";
import {jwtDecode} from "jwt-decode";
import '../../styles/FormStyles.css';
import AuthContext from "../../context/AuthProvider";
import {useNavigate, Link} from "react-router-dom";

const Login = () => {
    const {setAuth} = useContext(AuthContext);
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const response = await fetch("/api/auth/login", {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({email, password})
            });

            const data = await response.json();
            if (!response.ok) {
                setError(data.message);
                return
            }

            const decoded = jwtDecode(data.accessToken);
            setAuth({
                accessToken: data.accessToken,
                role: decoded.role
            });
            setEmail("");
            setPassword("");

            navigate("/home");

        } catch (err) {
            console.error(err);
        }
    };

    return (
        <React.Fragment>
            <div className="header">
                <h1>University Platform</h1>
            </div>
            <div className="container">
                <h2 className="header">Login</h2>
                {error && <div className="error general-error">{error}</div>}
                <form onSubmit={handleSubmit}>

                    <label>Email</label>
                    <input type="email" placeholder="Enter email" value={email}
                           onChange={(e) => setEmail(e.target.value)} required/>

                    <label>Password</label>
                    <input type="password" placeholder="Enter password" value={password}
                           onChange={(e) => setPassword(e.target.value)} required/>

                    <button className="btn btn-submit" type="submit">Login</button>
                </form>

                <p>Don't have an account? <Link to="/register">Register</Link></p>
            </div>
        </React.Fragment>
    );
};

export default Login;