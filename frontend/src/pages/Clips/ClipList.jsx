import { createContext, useEffect, useState } from "react";
import ClipCard from "./ClipCard";
import Spinner from "../../components/Spinner";

import useFetch from '../../useFetch';
import PropTypes from 'prop-types';
import SpinnerCircular from "../../components/SpinnerCircular.jsx";

// export const ThemeContext = createContext(null)

function ClipList(props) {



	const { url, method, currentPage, totalPages, setIsLoaded } = props;

	const {data: clips, isLoading, fetchData} = useFetch();




	useEffect(() => {
		setIsLoaded(false);
		fetchData(`${url}?page=${currentPage}&size=${totalPages}`, method);
	}, [currentPage]);





	return (
		<>	
			{
				isLoading && 
				<div className="loading"> <SpinnerCircular/> </div>
			}
			{
				!isLoading && clips  &&
				<div className="clip-list">
					<ul>
						{clips.map((clip) => (
							<li key={clip.clipId}>
								{/* <p>{clip.clip_title}</p> */}
								{/* <ThemeContext.Provider value={clip}>
									<ClipCard clip={clip}/>
								</ThemeContext.Provider> */}

								<ClipCard clip={clip} setIsLoaded={setIsLoaded}/>
							</li>
						))}
					</ul>
				
				</div>}
		</>
	);
}



export default ClipList;


ClipList.propTypes = {
	url: PropTypes.string.isRequired,
	method: PropTypes.string.isRequired,
};