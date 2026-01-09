export const authFetch = async (url, options = {}, auth, setAuth) => {
    let accessToken = auth?.accessToken;

    const refreshToken = async () => {
        const refreshRes = await fetch(`/api/auth/refresh`, {
            method: 'POST',
            credentials: 'include'
        });

        if (!refreshRes.ok) {
            setAuth({accessToken: null, role: null});
            throw new Error('Session expired');
        }

        const refreshData = await refreshRes.json();
        accessToken = refreshData.accessToken;
        setAuth(prev => ({...prev, accessToken}));
        return accessToken;
    };

    if (!accessToken) {
        await refreshToken();
    }

    let response = await fetch(url, {
        ...options,
        headers: {
            ...options.headers,
            Authorization: accessToken ? `Bearer ${accessToken}` : undefined
        },
        credentials: 'include'
    });

    if (response.status === 401) {
        try {
            await refreshToken();

            response = await fetch(url, {
                ...options,
                headers: {
                    ...options.headers,
                    Authorization: `Bearer ${accessToken}`
                },
                credentials: 'include'
            });
        } catch (err) {
            throw new Error('Session expired');
        }
    }

    return response;
};