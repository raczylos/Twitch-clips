import { useState, useEffect } from 'react';

const useFetch = (url, options = null) => {
    const [data, setData] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    useEffect(() => {
        fetch(url, options)
            .then(res => res.json())
            .then(data => setData(data))
            .catch((error) => setError(error))
            .finally(() => setIsLoading(false))
    }, [url, options]);
    return {data, isLoading, error}
}
export default useFetch;