import React from 'react';
import LazyImage from "../../components/LazyImage";
import { Link } from "react-router-dom";
import { CiStreamOn } from "react-icons/ci";

type Props = { 
  clip: Clip;
};

function ClipCard({ clip }: Props) {
	return (
	  <>
		  <div className="clip-card">
			<div className="card-title w-[300px]">
			  <Link to={`/clip/${clip.clipId}`}>
				<h2>{clip.clipTitle}</h2>
			  </Link>
			</div>
  
			<div className="card-content">
			  <Link to={`/clip/${clip.clipId}`}>
				<LazyImage
				  src={clip.thumbnailUrl}
				  alt="Clip preview"
				  placeholderSrc="../../images/clip-placeholder.jpg"
				  className="w-full h-auto"
				/>
			  </Link>
			</div>
  
			<div className="card-action mt-2 flex justify-between">
			  <span className="flex items-center gap-2">
				<LazyImage
				  src={clip.streamerImageUrl}
				  alt="Streamer avatar"
				  placeholderSrc="../../images/avatar-placeholder.jpg"
				  className="w-10 h-10 rounded-full"
				/>
				{clip.broadcasterName}
			  </span>
  
			  <div className="flex items-center gap-1">
				<CiStreamOn className="text-lg" />
				<span>{clip.viewCount}</span>
			  </div>
			</div>
		  </div>

	  </>
	);
  }
  
  export default ClipCard;

