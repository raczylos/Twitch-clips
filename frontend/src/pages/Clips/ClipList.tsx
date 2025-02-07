import { useEffect, useState } from "react";
import ClipCard from "./ClipCard";
import Spinner from "../../components/Spinner";

import useFetch from '../../hooks/useFetch';
import PropTypes from 'prop-types';
import SpinnerCircular from "../../components/SpinnerCircular";
import React from 'react';

type props = {
	url: string,
	method: string,
	currentPage: number,
	pageSize: number,
	setTotalPages: Function;
	setIsPageLoaded: Function,
	isPageLoaded: boolean,
	startedAt: string,
	endedAt: string,
  }

function ClipList(props: props) {

    const { url, method, currentPage, pageSize, setIsPageLoaded, setTotalPages, isPageLoaded, startedAt, endedAt } = props;
    const {data: clipsData, isLoading: isClipsLoading, fetchData: fetchClipsData} = useFetch();
    const clips = clipsData as Clips | null;
    const [showSpinner, setShowSpinner] = useState(true);

    useEffect(() => {
        setIsPageLoaded(false);
        fetchClipsData(`${url}?page=${currentPage}&pageSize=${pageSize}&startedAt=${startedAt}&endedAt=${endedAt}`, method);
		
		
        setShowSpinner(true);
        const timer = setTimeout(() => {
            setShowSpinner(false);
        }, 150);

        return () => clearTimeout(timer);

    }, [currentPage]);


    useEffect(() => {
        if (!isClipsLoading && clips && !showSpinner) {
            setTotalPages(clips?.totalPages);
            setIsPageLoaded(true)
        }
    }, [clips, isClipsLoading, setIsPageLoaded, showSpinner]);


    return (
        <>	
            {
                (isClipsLoading  || !isPageLoaded || showSpinner) &&
                <div  className="flex justify-center mt-10">
                    <div> <SpinnerCircular/> </div>
                </div>
            }
            {
                !isClipsLoading && clips && !showSpinner && isPageLoaded &&
                <div className="clip-list">
                    <ul>
                        {clips.content.map((clip: Clip) => (
                            <li key={clip.clipId}>
                                <ClipCard clip={clip}/>
                            </li>
	
				        ))}
                    </ul>
				
                </div>
            }
        </>
    );
}



export default ClipList;


ClipList.propTypes = {
    url: PropTypes.string.isRequired,
    method: PropTypes.string.isRequired,
};