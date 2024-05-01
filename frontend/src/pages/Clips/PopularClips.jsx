import { useState } from "react";
import Pagination from "../../components/Pagination";
import ClipList from "./ClipList";


function PopularClips() {
    const url = 'http://localhost:8080/api/v1/twitch/clips'
    const [page, setPage] = useState(1)
    const totalPages = 12;
    const [isLoaded, setIsLoaded] = useState(false);

    const onPageChange = (newPage) => {
        setPage(newPage);
    };





    return (
        <>
            <ClipList url={url} method={"get"} currentPage={page} totalPages={totalPages} setIsLoaded={setIsLoaded} />
            
            {isLoaded && <Pagination currentPage={page} totalPages={totalPages} onPageChange={onPageChange}/>}
        </>

    )


}

export default PopularClips;