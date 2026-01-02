import {createContext, useEffect, useState} from 'react';
import {jwtDecode} from "jwt-decode";

const AuthContext = createContext({});

export const AuthProvider = ({children}) => {
    const [auth, setAuth] = useState({
        accessToken: null,
        role: null
    });

    const [loadingAuth, setLoadingAuth] = useState(true);

    useEffect(() => {
        const initializeAuth = async () => {
            try {
                const res = await fetch('/api/auth/refresh', {
                    method: 'POST',
                    credentials: 'include'
                });

                if (!res.ok) {
                    setAuth({accessToken: null, role: null});
                    setLoadingAuth(false);
                    return;
                }

                const data = await res.json();
                const decoded = jwtDecode(data.accessToken);
                setAuth({
                    accessToken: data.accessToken,
                    role: decoded.role
                });
            } catch (err) {
                console.error(err);
                setAuth({accessToken: null, role: null});
            } finally {
                setLoadingAuth(false);
            }
        };

        initializeAuth();
    }, []);

    return (
        <AuthContext.Provider value={{auth, setAuth}}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthContext;