import { useState, useEffect } from "react";
import Pagination from "../../components/Pagination";
import ClipList from "./ClipList";
import React from 'react';

function PopularClips() {
    const url = 'http://localhost:8080/api/v1/twitch/clips/popular'
    const [page, setPage] = useState(1)
    const pageSize = 12;
    const [isPageLoaded, setIsPageLoaded] = useState(false);
    const [totalPages, setTotalPages] = useState(0);

    const onPageChange = (newPage: number) => {
        setPage(newPage);
    };
    const startedAt = "2019-01-01";
    const endedAt = "2025-02-05";

    return (
        <>
            <ClipList url={url} method={"get"} currentPage={page} pageSize={pageSize} setTotalPages={setTotalPages} setIsPageLoaded={setIsPageLoaded} isPageLoaded={isPageLoaded} startedAt={startedAt} endedAt={endedAt} />
        
            {isPageLoaded && <Pagination currentPage={page} totalPages={totalPages} onPageChange={onPageChange}/>}
        </>

    )


}

export default PopularClips;