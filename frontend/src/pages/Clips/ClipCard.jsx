import { Link } from "react-router-dom";
import { useState, useEffect } from "react";
import SpinnerCircular from "../../components/SpinnerCircular";

function ClipCard(props) {
	const { clip, setIsLoaded } = props;

	const [imageLoaded, setImageLoaded] = useState(false);

	useEffect(() => {
		const img = new Image()
		img.onload = () => {
			setImageLoaded(true)
			setIsLoaded(true)
		}
		img.src = clip.thumbnailUrl
	}, [clip])
	

	return (
		<>
			
			{imageLoaded && <div className="clip-card">
				<div className="card-title">
					<Link to={`/clip/${clip.clipId}`}>
						<h2>{clip.clipTitle}</h2>
					</Link>
                </div>
				<div className="card-content">
					<Link to={`/clip/${clip.clipId}`}>
						<img 
							src={clip.thumbnailUrl}
							alt="thumbnail" 
							onLoad={() => {
								setImageLoaded(true)
								setIsLoaded(true)}
							} 
						/>
						
					</Link>
				</div>
				<div className="card-action">
					<button>test button</button>
				</div>
			</div>}
		</>
	);
}

export default ClipCard;
