import { createContext, useEffect, useState } from "react";
import ClipCard from "./ClipCard";
import Spinner from "../../components/Spinner";

import useFetch from '../../Fetch';
import PropTypes from 'prop-types';

// export const ThemeContext = createContext(null)

function ClipList(props) {



	const { url } = props;
	// const [clips, setClips] = useState([]);
	const [username, setUsername] = useState("Lightt__");
	const [page, setPage] = useState(1);
	const [clipsPerPage, setClipsPerPage] = useState(9);

	// const [isLoading, setIsLoading] = useState(true);



	const { clips, isLoading } = useFetch(`${url}/${username}?page=${page}&clips_per_page=${clipsPerPage}`);

	// `http://127.0.0.1:5000/favorite_clips/${username}?page=${page}&clips_per_page=${clipsPerPage}`
	// useEffect(() => {
	// 	fetch(`http://127.0.0.1:5000/favorite_clips/${username}?page=${page}&clips_per_page=${clipsPerPage}`)
	// 		.then((response) => response.json())
	// 		.then((clips) => {
	// 			setClips(clips)
	// 			setIsLoading(false)
	// 		})
	// 		.catch((error) => console.error(error));
	// }, []);

	return (
		<>	
			{
				isLoading || !clips && 
				<div className="loading"> <Spinner/> </div>
			}
			{
				!isLoading && clips && 
				<div className="clip-list">
					<ul>
						{clips.map((clip) => (
							<li key={clip.clip_id}>
								{/* <p>{clip.clip_title}</p> */}
								{/* <ThemeContext.Provider value={clip}>
									<ClipCard clip={clip}/>
								</ThemeContext.Provider> */}

								<ClipCard clip={clip} />
							</li>
						))}
					</ul>
					ClipList
				
				</div>}
		</>
	);
}



export default ClipList;


ClipList.propTypes = {
	url: PropTypes.string.isRequired,
};