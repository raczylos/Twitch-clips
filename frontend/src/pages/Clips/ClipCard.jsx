import { Link } from "react-router-dom";

function ClipCard(props) {
	const { clip } = props;

	console.log("ClipCard", clip);
	return (
		<>
			<div className="clip-card">
				<div className="card-title">
					<Link to={`/clip/${clip.clip_id}`}>
						<h2>{clip.clip_title}</h2>
					</Link>
                </div>
				<div className="card-content">
					<Link to={`/clip/${clip.clip_id}`}>
						<img src={clip.thumbnail_url} alt="thumbnail" ></img>
					</Link>
				</div>
				<div className="card-action">
					<button>test button</button>
				</div>
			</div>
		</>
	);
}

export default ClipCard;
