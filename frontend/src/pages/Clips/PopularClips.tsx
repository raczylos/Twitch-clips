import { useState } from "react";
import Pagination from "../../components/Pagination";
import ClipList from "./ClipList";
import React from 'react';
import ClipFilter from "../../components/ClipFilter";

function PopularClips() {
	const url = 'http://localhost:8080/api/v1/twitch/clips/popular'
	const [page, setPage] = useState(0)
	const pageSize = 24;
	const [isPageLoaded, setIsPageLoaded] = useState(false);
	const [totalPages, setTotalPages] = useState(0);

	const onPageChange = (newPage: number) => {
		setPage(newPage);
	};
	const startedAt = "2019-01-01T00:00:00Z";
	const endedAt = "2025-03-08T23:59:59Z";

	return (
		<>
			<h1 className="text-center">Most popular clips</h1>
			<ClipFilter></ClipFilter>
			<ClipList url={url} method={"get"} currentPage={page} pageSize={pageSize} setTotalPages={setTotalPages} setIsPageLoaded={setIsPageLoaded} isPageLoaded={isPageLoaded} startedAt={startedAt} endedAt={endedAt} />
		
			{isPageLoaded && <Pagination currentPage={page} totalPages={totalPages} onPageChange={onPageChange}/>}
		</>

	)


}

export default PopularClips;